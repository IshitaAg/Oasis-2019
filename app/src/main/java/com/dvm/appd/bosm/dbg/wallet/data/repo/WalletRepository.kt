package com.dvm.appd.bosm.dbg.wallet.data.repo

import android.util.Log
import com.dvm.appd.bosm.dbg.auth.data.repo.AuthRepository
import com.dvm.appd.bosm.dbg.profile.views.fragments.ProfileFragment
import com.dvm.appd.bosm.dbg.profile.views.fragments.TransactionResult
import com.dvm.appd.bosm.dbg.shared.MoneyTracker
import com.dvm.appd.bosm.dbg.shared.NetworkChecker
import com.dvm.appd.bosm.dbg.wallet.data.retrofit.WalletService
import com.dvm.appd.bosm.dbg.wallet.data.retrofit.dataclasses.*
import com.dvm.appd.bosm.dbg.wallet.data.room.WalletDao
import com.dvm.appd.bosm.dbg.wallet.data.room.dataclasses.StallData
import com.dvm.appd.bosm.dbg.wallet.data.room.dataclasses.StallItemsData
import com.dvm.appd.bosm.dbg.wallet.data.room.dataclasses.*
import com.dvm.appd.bosm.dbg.wallet.data.retrofit.dataclasses.PaytmPojo
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.gson.JsonObject
import com.paytm.pgsdk.PaytmClientCertificate
import com.paytm.pgsdk.PaytmOrder
import com.paytm.pgsdk.PaytmPGService
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import java.lang.Exception

class WalletRepository(val walletService: WalletService, val walletDao: WalletDao, val authRepository: AuthRepository, val moneyTracker: MoneyTracker, val networkChecker: NetworkChecker) {

    private val jwt
    get() = authRepository.getUser().toSingle().flatMap { return@flatMap Single.just("jwt ${it.jwt}") }
    private val userId
    get()  = authRepository.getUser().toSingle().flatMap { return@flatMap Single.just(it.userId.toInt()) }

    lateinit var l1: ListenerRegistration
    lateinit var l2: ListenerRegistration
    private val db = FirebaseFirestore.getInstance()

    init {
        addOrderListener()
        addTicketListener()
        fetchAllStalls()
//        getTicketInfo().subscribe()
    }

    fun fetchAllStalls(): Completable {
        Log.d("check", "called")
        return walletService.getAllStalls()
            .doOnSuccess { response ->
                Log.d("check", response.body().toString())
                Log.d("checkfetch", response.code().toString())
                when (response.code()) {
                    200 -> {
                        var stallList: List<StallData> = emptyList()
                        var itemList: List<StallItemsData> = emptyList()
                        response.body()!!.forEach { stall ->
                            stallList = stallList.plus(stall.toStallData())
                            itemList = itemList.plus(stall.toStallItemsData())
                        }
                        Log.d("checkwmr", itemList.toString())
                        walletDao.deleteAllStalls()
                        walletDao.deleteAllStallItems()
                        walletDao.insertAllStalls(stallList)
                        walletDao.insertAllStallItems(itemList)

                    }
                    else -> {
                        Log.d("checke", response.errorBody()!!.string())
                    }
                }
            }.ignoreElement()
            .doOnError {
                Log.d("checke", it.message)
            }.subscribeOn(Schedulers.io())

    }

    fun getAllStalls(): Observable<List<StallData>> {
        Log.d("check", "calledg")

       /* if (networkChecker.isConnected() == false) {
            return walletDao.getAllStalls().toObservable().subscribeOn(Schedulers.io())
                .doOnError {
                    Log.d("checkre", it.toString())
                }
        }*/

        return walletDao.getAllStalls().toObservable()
            .subscribeOn(Schedulers.io())
            .doOnError {
                Log.d("checke", it.toString())
            }
    }

    fun getItemsForStall(stallId: Int): Observable<MutableList<Pair<String, List<ModifiedStallItemsData>>>> {

        return walletDao.getModifiedStallItemsById(stallId, true).toObservable()
            .flatMap {

                var stallItems: MutableList<Pair<String, List<ModifiedStallItemsData>>> = arrayListOf()
                var categoryItems: MutableList<ModifiedStallItemsData> = arrayListOf()

                for ((index, item) in it.listIterator().withIndex()){

                    categoryItems.add(item)

                    if (index != it.lastIndex && it[index].category != it[index+1].category){
                        stallItems.add(Pair(item.category, categoryItems))
                        categoryItems = arrayListOf()
                    }
                    else if (index == it.lastIndex){
                        stallItems.add(Pair(item.category, categoryItems))
                        categoryItems = arrayListOf()
                    }
                }

               return@flatMap Observable.just(stallItems)
            }
            .doOnError {
              Log.d("checke",it.toString())
            }
            .subscribeOn(Schedulers.io())
    }

    private fun StallsPojo.toStallData(): StallData {
        return StallData(stallId, stallName, closed)
    }

    private fun StallsPojo.toStallItemsData(): List<StallItemsData> {

        var itemList: List<StallItemsData> = emptyList()

        items.forEach {
            itemList =
                itemList.plus(StallItemsData(it.itemId, it.itemName, it.stallId, it.category.trim(), it.price, it.isAvailable, it.isVeg))
        }
        return itemList
    }

    fun updateOrders(): Completable{
        Log.d("CheckJWT", jwt.blockingGet().toString())
        return walletService.getAllOrders(jwt.blockingGet().toString()).subscribeOn(Schedulers.io())
            .doOnSuccess {response ->
                when(response.code()){
                    200 -> {

                        var orders: MutableList<OrderData> = arrayListOf()
                        var orderItems: MutableList<OrderItemsData> = arrayListOf()

                        response.body()!!.forEach {
                            orders.addAll(it.toOrderData())
                            orderItems.addAll(it.toOrderItemsData())
                        }

                        Log.d("Orders", orders.toString())
                        Log.d("Orders", orderItems.toString())

                        walletDao.deleteAndInsertOrders(orders)
                        walletDao.deleteAndInsertOrderItems(orderItems)

                    }

                    401 -> {
                        Log.d("PlaceOrder", "Success Error: 401")
                        throw Exception("Wrong credentials: Login again")
                    }

                    in 400..499 -> {
                        throw Exception(response.message())
                    }

                    else -> {
                        throw Exception("Server error")
                    }
                }
            }
            .doOnError {
                Log.e("GetOrder", "Error", it)
            }
            .ignoreElement()

    }

    private fun AllOrdersPojo.toOrderData(): List<OrderData> {

        var orderData: MutableList<OrderData> = arrayListOf()
        orders.forEach {
            orderData.add(
                OrderData(
                    orderId = it.id,
                    shell = it.shell,
                    otp = it.otp,
                    otpSeen = it.otpSeen,
                    status = it.status,
                    price = it.price,
                    vendor = it.vendor.vendorName,
                    rating = it.rating
                )
            )
        }
        return orderData
    }

    private fun AllOrdersPojo.toOrderItemsData(): List<OrderItemsData> {

        var orderItemsData: MutableList<OrderItemsData> = arrayListOf()
        orders.forEach {
            it.items.forEach { orderItemsPojo ->
                orderItemsData.add(
                    OrderItemsData(
                        itemName = orderItemsPojo.itemName,
                        itemId = orderItemsPojo.itemId,
                        quantity = orderItemsPojo.quantity,
                        price = orderItemsPojo.price,
                        orderId = it.id,
                        id = 0
                    )
                )
            }
        }

        return orderItemsData
    }

    fun getAllOrders(): Flowable<List<ModifiedOrdersData>> {

        return walletDao.getOrdersData().subscribeOn(Schedulers.io())
            .flatMap {

                var ordersList: MutableList<ModifiedOrdersData> = arrayListOf()
                var itemsList: MutableList<ModifiedItemsData> = arrayListOf()

                for ((index, item) in it.listIterator().withIndex()) {

                    itemsList.add(
                        ModifiedItemsData(
                            itemName = item.itemName, itemId = item.itemId,
                            quantity = item.quantity, price = item.price
                        )
                    )

                    if (index != it.lastIndex && it[index].orderId != it[index + 1].orderId) {

                        ordersList.add(
                            ModifiedOrdersData(
                                orderId = item.orderId,
                                shell = item.shell,
                                otp = item.otp,
                                otpSeen = item.otpSeen,
                                status = item.status,
                                totalPrice = item.totalPrice,
                                vendor = item.vendor,
                                items = itemsList,
                                rating = item.rating
                            )
                        )

                        itemsList = arrayListOf()
                    } else if (index == it.lastIndex) {
                        ordersList.add(
                            ModifiedOrdersData(
                                orderId = item.orderId,
                                shell = item.shell,
                                otp = item.otp,
                                otpSeen = item.otpSeen,
                                status = item.status,
                                totalPrice = item.totalPrice,
                                vendor = item.vendor,
                                items = itemsList,
                                rating = item.rating
                            )
                        )

                        itemsList = arrayListOf()
                    }
                }

                return@flatMap Flowable.just(ordersList)

            }
    }

    fun getOrderById(orderId: Int): Flowable<ModifiedOrdersData>{
        return walletDao.getOrderById(orderId).subscribeOn(Schedulers.io())
            .flatMap {

                Log.d("OrderDetailRepo", it.toString())
                var order: ModifiedOrdersData
                var itemsList: MutableList<ModifiedItemsData> = arrayListOf()

                it.forEach{item ->

                    itemsList.add(
                        ModifiedItemsData(
                            itemName = item.itemName, itemId = item.itemId,
                            quantity = item.quantity, price = item.price
                        )
                    )
                }

                order = ModifiedOrdersData(it.last().orderId, it.last().shell, it.last().otp, it.last().otpSeen, it.last().status, it.last().totalPrice, it.last().vendor, itemsList, it.last().rating)

                return@flatMap Flowable.just(order)
            }
    }

    fun insertCartItems(cartItem: CartData): Completable {
        return walletDao.insertCartItems(cartItem).subscribeOn(Schedulers.io())
    }

    fun deleteCartItem(itemId: Int): Completable {
        return walletDao.deleteCartItem(itemId).subscribeOn(Schedulers.io())
    }

    fun placeOrder(): Completable {

        return walletDao.getAllCartItems().subscribeOn(Schedulers.io())
            .firstOrError()
            .map {

                var orderJsonObject = JsonObject()
                var orders = JsonObject()
                var items: MutableList<Pair<Int, Int>> = arrayListOf()
                for ((index, item) in it.listIterator().withIndex()) {

                    items.add(Pair(item.itemId, item.quantity))

                    if (index != it.lastIndex && it[index].vendorId != it[index + 1].vendorId) {

                        orders.add("${it[index].vendorId}", JsonObject().also {
                            items.forEach { pair ->
                                it.addProperty(pair.first.toString(), pair.second)
                            }
                        })

                        items = arrayListOf()
                    } else if (index == it.lastIndex) {

                        orders.add("${it[index].vendorId}", JsonObject().also {
                            items.forEach { pair ->
                                it.addProperty(pair.first.toString(), pair.second)
                            }
                        })

                        items = arrayListOf()
                    }

                }

                orderJsonObject.add("orderdict", orders)
                Log.d("PlaceOrder", "$orderJsonObject")
                return@map orderJsonObject
            }
            .flatMapCompletable {body ->
                Log.d("CheckJWT", jwt.blockingGet().toString())
                return@flatMapCompletable walletService.placeOrder(jwt.blockingGet().toString(), body).subscribeOn(Schedulers.io())
                    .doOnSuccess {response ->

                        when (response.code()) {

                            200 -> {
                                walletDao.clearCart().subscribeOn(Schedulers.io()).subscribe()
                            }

                            400 -> {
                                Log.d("PlaceOrder", "Success Error: 400")
                                throw Exception("App error. Contact DVM officials")
                            }

                            401 -> {
                                Log.d("PlaceOrder", "Success Error: 401")
                                throw Exception("Wrong credentials: Login again")
                            }

                            403 -> {
                                Log.d("PlaceOrder", "Success Error: 403")
                                throw Exception("User banned: Contact officials")
                            }

                            404 -> {
                                Log.d("PlaceOrder", "Success Error: 404")
                                walletDao.clearCart().subscribeOn(Schedulers.io()).subscribe()
                                throw Exception(response.message())
                            }

                            412 -> {

                                Log.d("PlaceOrder", "Success Error: 412")
                                //walletDao.clearCart().subscribeOn(Schedulers.io()).subscribe()
                                val message = JSONObject(response.errorBody()!!.string()).getString("display_message")
                                throw Exception(message)
                            }

                            in 400..499 -> {
                                throw Exception(response.message())
                            }

                            else -> {
                                throw Exception("Server error")
                            }

                        }

                    }
                    .doOnError {
                        Log.e("PlaceOrder", "Error", it)
                    }
                    .ignoreElement()
            }
    }

    fun getAllModifiedCartItems(): Flowable<List<ModifiedCartData>> {
        return walletDao.getAllModifiedCartItems().subscribeOn(Schedulers.io())
    }

    fun updateCartItems(itemId: Int, quantity: Int): Completable {
        return walletDao.updateCartItem(quantity, itemId).subscribeOn(Schedulers.io())
    }

    fun updateOtpSeen(orderId: Int): Completable {

        val body = JsonObject().also {
            it.addProperty("order_id", orderId)
        }

        return walletService.makeOtpSeen(jwt.blockingGet().toString(), body).subscribeOn(Schedulers.io())
            .doOnSuccess {response ->

                when (response.code()) {

                    200 -> {

                    }

                    401 -> {
                        Log.d("PlaceOrder", "Success Error: 401")
                        throw Exception("Wrong credentials: Login again")
                    }

                    in 400..499 -> {
                        throw Exception(response.message())
                    }

                    else -> {
                        throw Exception("Server error")
                    }

                }
            }
            .doOnError {

            }
            .ignoreElement()
    }

    fun addMoneyBitsian(amount: Int): Single<TransactionResult> {

        val body = JsonObject().also {
            it.addProperty("amount", amount)
        }

        Log.d("check", body.toString())

        return authRepository.getUser()
            .toSingle()
            .flatMap {
                if(it.isBitsian == false){
                    throw Exception("Only BITSians can add money through SWD!!")
                }
                walletService.addMoneyBitsian("jwt ${it.jwt}", body).map { response ->
                    Log.d("check", response.code().toString())

                    when (response.code()) {
                        200 -> TransactionResult.Success
                        in 400..499 -> TransactionResult.Failure(response.errorBody()!!.string())
                        else -> TransactionResult.Failure("Something went wrong!!")
                    }
                }.doOnError { throwable ->
                    Log.d("checke", throwable.toString())
                }
            }.subscribeOn(Schedulers.io())

    }

    fun getBalance(): Flowable<Int> {
        return moneyTracker.getBalance().doOnError {
            Log.d("checke", it.toString())
        }
    }

    fun transferMoney(id:Int,amount:Int):Single<TransactionResult>{

        val body = JsonObject().also {
            it.addProperty("id",id)
            it.addProperty("amount",amount)
        }
        Log.d("check",body.toString())
        return authRepository.getUser()
            .toSingle()
            .flatMap {
                if(it.isBitsian==false){
                    throw Exception("Only BITSians can do money transfers!")
                }
                walletService.transferMoney("jwt ${it.jwt}",body).map {response ->
                    Log.d("check",response.code().toString())
                    when(response.code()){
                        200 -> TransactionResult.Success
                        in 400..499 -> TransactionResult.Failure(response.errorBody()!!.string())
                        else -> TransactionResult.Failure("Something went wrong!!")

                    }
                }.doOnError { throwable ->
                    Log.d("checke", throwable.toString())
                }
            }.subscribeOn(Schedulers.io())

    }

    fun rateOrder(orderId: Int, shell: Int, rating: Int): Completable{

        val body = JsonObject().also {
            it.addProperty("order_shell_id", shell)
            it.addProperty("order_id", orderId)
            it.addProperty("rating", rating)
            it.addProperty("comments", "")
        }

        return walletService.rateOrder(jwt.blockingGet().toString(), body, orderId, shell)
            .map {
                when(it.code()){

                    200 -> {
                        Log.d("Rated", "rated")
                        updateOrders().subscribe()
                    }

                    401 -> {
                        Log.d("PlaceOrder", "Success Error: 401")
                        throw Exception("Wrong credentials: Login again")
                    }

                    in 400..499 -> {
                        throw Exception(it.message())
                    }
                    else -> {
                        throw Exception("Server error")
                    }
                }
            }
            .subscribeOn(Schedulers.io())
            .ignoreElement()
    }

    fun getTicketInfo(): Completable{
        return walletService.getAllTickets(jwt.blockingGet().toString()).subscribeOn(Schedulers.io())
            .doOnSuccess {response ->

                Log.d("TicketsApi", "${response.body()}")
                when(response.code()){

                    200 -> {

                        var tickets: MutableList<TicketsData> = arrayListOf()

                        response.body()!!.combos.forEach{

                            tickets.add(it.toTickets())
                        }

                        response.body()!!.shows.forEach {

                            tickets.add(it.toTickets())
                        }

                        Log.d("TicketsApi", "${tickets}")
                        walletDao.updateTickets(tickets)


                    }

                    401 -> {
                        Log.d("PlaceOrder", "Success Error: 401")
                        throw Exception("Wrong credentials: Login again")
                    }

                    in 400..499 -> {
                        throw Exception(response.message())
                    }

                    else -> {
                        throw Exception("Server error")
                    }
                }
            }
            .ignoreElement()
    }

    private fun ComboPojo.toTickets(): TicketsData{
        return TicketsData(id, name, price, "combo", shows.map { it.name }.joinToString(","), 0)
    }

    private fun ShowsPojo.toTickets(): TicketsData{
        return TicketsData(id, name, price,"show", null,0)
    }

    fun updateUserTickets(): Completable{
         return walletService.getUserTickets(jwt.blockingGet().toString()).subscribeOn(Schedulers.io())
            .doOnSuccess {response ->

                Log.d("Tickets", "$response")
                when(response.code()){

                    200 -> {
                        var userTickets: MutableList<UserShows> = arrayListOf()

                        response.body()!!.shows.forEach {
                            userTickets.add(it.toUserShows())
                        }

                        walletDao.updateUserTickets(userTickets)
                    }

                    401 -> {
                        Log.d("PlaceOrder", "Success Error: 401")
                        throw Exception("Wrong credentials: Login again")
                    }

                    in 400..499 -> {
                        throw Exception(response.message())
                    }

                    else -> {
                        throw Exception("Server error")
                    }

                }
            }
            .ignoreElement()
    }

    private fun UserShowPojo.toUserShows(): UserShows{
        return UserShows(id, showName, usedCount, unusedCount)
    }

    fun getAllUserShows(): Flowable<List<UserShows>>{
        return walletDao.getAllUserTickets().subscribeOn(Schedulers.io())
    }

    fun getAllTicketData(): Flowable<List<ModifiedTicketsData>>{

        return walletDao.getAllTickets().subscribeOn(Schedulers.io())
    }

    fun insertTicketsCart(tickets: TicketsCart): Completable{
        return walletDao.insertTicketCart(tickets).subscribeOn(Schedulers.io())
    }

    fun deleteTicektsCartItem(id: Int): Completable{
        return walletDao.clearTicketsCartItem(id).subscribeOn(Schedulers.io())
    }

    fun updateTicketsCart(quantity: Int, id: Int): Completable{
        return walletDao.updateTicketsCart(quantity, id).subscribeOn(Schedulers.io())
    }

    fun buyTickets(): Completable{

        return walletDao.getTicketsCart().subscribeOn(Schedulers.io())
            .firstOrError()
            .map {

                var ticketBody = JsonObject()
                var individualBody = JsonObject()
                var comboBody = JsonObject()

                for (item in it){

                    if (item.type == "show"){
                        individualBody.addProperty("${item.ticketId}", item.quantity)
                    }

                    if (item.type == "combo"){
                        comboBody.addProperty("${item.ticketId}", item.quantity)
                    }
                }

                ticketBody.add("individual", individualBody)
                ticketBody.add("combos", comboBody)
                Log.d("Tickets", "$ticketBody")

                return@map ticketBody
            }
            .flatMapCompletable{
                return@flatMapCompletable walletService.buyTickets(jwt.blockingGet().toString(), it).subscribeOn(Schedulers.io())
                    .doOnSuccess {response ->

                        Log.d("TicketsBuy", response.code().toString())
                        when(response.code()){

                            200 -> {
                                Log.d("TicketsBuy", "Yusss")
                                walletDao.clearTicketsCart().subscribeOn(Schedulers.io()).subscribe()
                            }

                            401 -> {
                                Log.d("PlaceOrder", "Success Error: 401")
                                throw Exception("Wrong credentials: Login again")
                            }

                            in 400..499 -> {
                                throw Exception(response.message())
                            }

                            else -> {
                                throw Exception("Server error")
                            }
                        }
                    }
                    .ignoreElement()
            }
    }

    fun addOrderListener(){
        l1 = db.collection("orders").whereEqualTo("userid", userId.blockingGet())
            .addSnapshotListener { snapshot, exception ->

                if (exception != null) {
                    Log.e("WalletRepo", "Listen Failed", exception)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    Log.d("WalletRepo", "Firebase $snapshot")
                    updateOrders().subscribe()
                }
            }
    }

    fun addTicketListener(){
        l2 = db.collection("tickets").document("${userId.blockingGet()}").collection("shows")
            .addSnapshotListener { querySnapshot, exception ->

                if (exception != null){
                    Log.e("WalletRepo", "Listen Failed", exception)
                    return@addSnapshotListener
                }

                if (querySnapshot != null){
                    Log.d("WalletRepo", "Firebase $querySnapshot")
                    updateUserTickets().subscribe()
                }
            }
    }

    fun disposeListener(){
        l1.remove()
        l2.remove()
    }

    //Paytm

    //use prodPGService instead of staging when production level
    fun getCheckSum(stagingPGService: PaytmPGService, prodPGService: PaytmPGService, fragment: ProfileFragment, txnAmount: String): Completable{
        return walletService.getCheckSum(
            PaytmPojo(
                "Merchant Id from paytm", "generate random UUID", "generate random UUID", "Channel Id from paytm",
                txnAmount, "", "", "Industry type from paytm"
            )
        ).subscribeOn(Schedulers.io())
            .doOnSuccess {response ->

                when(response.code()){
                    200 ->{
                        val paraMap = HashMap<String, String>()
                        paraMap["MID"] = "same as before"
                        paraMap["ORDER_ID"] = "same as before"
                        paraMap["CUST_ID"] = "same as before"
                        paraMap["CHANNEL_ID"] = "same as before"
                        paraMap["TXN_AMOUNT"] = "same as before"
                        paraMap["WEBSITE"] = "same as before"
                        paraMap["CALLBACK_URL"] = "same as before"
                        paraMap["CHECKSUMHASH"] = response.body()!!.checksumHash
                        paraMap["INDUSTRY_TYPE_ID"] = "same as before"

                        //creating paytm order object
                        val order = PaytmOrder(paraMap)

                        //initialize paytm service(for production level) pass Certificate instead null if needed
                        val certificate = PaytmClientCertificate("password for client side certificate", "file name for client side certificate")
                        stagingPGService.initialize(order, null)

                        //needs activity context for callback
                        stagingPGService.startPaymentTransaction(fragment.context, true, true, fragment)
                    }
                    else -> {}
                }
            }
            .ignoreElement()
    }
}