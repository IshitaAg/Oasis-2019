package com.dvm.appd.oasis.dbg.more

import com.dvm.appd.oasis.dbg.more.dataClasses.Comedian
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.subjects.BehaviorSubject

class ComediansVoting {

    private val database = FirebaseFirestore.getInstance()
    private val comediansSubject = BehaviorSubject.create<List<Comedian>>()
    private val votingStatus = BehaviorSubject.create<Boolean>()

    init{
       database.collection("voting").document("info").addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
           if(documentSnapshot!=null){
               val comedians = ArrayList<Comedian>()
               if(documentSnapshot.getBoolean("enabled")==true) {
                   database.collection("voting").document("info").collection("comedians").get()
                       .addOnSuccessListener { docs ->
                           for (doc in docs) {
                               comedians.add(Comedian(doc.id))
                           }
                       }
               }
               comediansSubject.onNext(comedians)
               // votingStatus.onNext(documentSnapshot.getBoolean("enabled")!!)
           }
       }

    }

    fun getComedians()= comediansSubject.toFlowable(BackpressureStrategy.LATEST)
    fun getStatus()=votingStatus.toFlowable(BackpressureStrategy.LATEST)
    fun vote(comedianName:String):Completable{
       return Completable.fromAction{
           database.collection("voting").document("info").collection("comedians").document(comedianName).update("votes",FieldValue.increment(1))
       }
    }
}