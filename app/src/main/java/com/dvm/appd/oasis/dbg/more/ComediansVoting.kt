package com.dvm.appd.oasis.dbg.more

import com.dvm.appd.oasis.dbg.more.dataClasses.Comedian
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.subjects.BehaviorSubject

class ComediansVoting {

    private val database = FirebaseFirestore.getInstance()
    private val comediansSubject = BehaviorSubject.create<List<Comedian>>()
    private val votingStatus = BehaviorSubject.create<Boolean>()

    init{
        database.collection("voting").document("info").collection("comedians").get().addOnSuccessListener {documents->
                var comedians = ArrayList<Comedian>()
            for(doc in documents){
                 comedians.add(Comedian(doc.id))
            }
            comediansSubject.onNext(comedians)
        }

    }
}