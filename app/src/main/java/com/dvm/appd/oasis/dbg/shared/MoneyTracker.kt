package com.dvm.appd.oasis.dbg.shared

import com.dvm.appd.oasis.dbg.auth.data.repo.AuthRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.BehaviorSubject
import java.lang.Exception

class MoneyTracker(val authRepository: AuthRepository) {

    private val db = FirebaseFirestore.getInstance()
    private val subject = BehaviorSubject.create<Int>()
    private val tokenSubject = BehaviorSubject.create<Int>()
    lateinit var l3: ListenerRegistration

    init {

        addUserListener()

    }

    fun getBalance():Flowable<Int> = subject.toFlowable(BackpressureStrategy.LATEST)
    fun getTokens():Flowable<Int> = tokenSubject.toFlowable(BackpressureStrategy.LATEST)

    fun addUserListener(){
        authRepository.getUser()
            .subscribe{
                l3 = db.collection("users").document(it.userId).addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
                    val balance: Int
                    val tokens:Int
                    //TODO Determine correct default value
                    balance = try {
                        (documentSnapshot?.getLong("total_balance"))!!.toInt()
                    } catch (e: Exception) {
                        Integer.MAX_VALUE
                    }

                    tokens = try {
                        (documentSnapshot?.getLong("tokens"))!!.toInt()
                    }catch (e:Exception){
                        Integer.MAX_VALUE
                    }
                    subject.onNext(balance)
                    tokenSubject.onNext(tokens)
                }

            }
    }

    fun disposeListener(){
        l3.remove()
    }
}