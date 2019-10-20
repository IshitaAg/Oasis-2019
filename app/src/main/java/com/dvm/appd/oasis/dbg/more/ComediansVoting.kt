package com.dvm.appd.oasis.dbg.more

import android.util.Log
import com.dvm.appd.oasis.dbg.more.dataClasses.Comedian
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.subjects.BehaviorSubject
import java.lang.Exception

class ComediansVoting {

    private val database = FirebaseFirestore.getInstance()
    private val comediansSubject = BehaviorSubject.create<List<Comedian>>()
    private val votingStatus = BehaviorSubject.create<Boolean>()

    init{
       database.collection("voting").document("info").addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
           if(documentSnapshot!=null){
               Log.d("N2O Voteing", "Recived Document = $documentSnapshot")
               try {
                   votingStatus.onNext(documentSnapshot.getBoolean("enabled")!!)
               }catch (e:Exception){
                   Log.d("checke",e.toString())
               }
              /* votingStatus.onNext(documentSnapshot.getBoolean("enabled")!!)*/

               val comedians = ArrayList<Comedian>()
               if(documentSnapshot.getBoolean("enabled")==true) {
                   Log.d("N2O Voteing", "Entered True statement")
                   database.collection("voting").document("info").collection("comedians").get()
                       .addOnSuccessListener { docs ->
                           Log.d("N2O Voteing", "Document = $docs")
                           for (doc in docs) {
                               Log.d("N2O Voteing", "EDoc = $doc")
                               comedians.add(Comedian(doc.id))
                               comediansSubject.onNext(comedians)
                           }
                       }
               }

               comediansSubject.onNext(comedians)
           }
       }

    }

    fun getComedians()= comediansSubject.toFlowable(BackpressureStrategy.LATEST)
    fun getStatus()=votingStatus.toFlowable(BackpressureStrategy.LATEST)
    fun vote(comedianName:String):Completable{
       return Completable.fromAction{
           database.collection("voting").document("info").collection("comedians").document(comedianName).update("votes",FieldValue.increment(1)).addOnSuccessListener {
               Log.d("N2O Voteing", "Entered Success statement")
           }.addOnFailureListener {
               throw it
           }
       }
    }
}