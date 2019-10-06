package com.dvm.appd.oasis.dbg.elas.model.room

import androidx.room.*
import com.dvm.appd.oasis.dbg.elas.model.dataClasses.CombinedQuestionOptionDataClass
import com.dvm.appd.oasis.dbg.elas.model.retrofit.PlayerRankingResponse
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface ElasDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertQuestions(questions: List<QuestionData>): Single<Unit>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOptions(options: List<OptionData>): Single<Unit>

    @Insert(onConflict =  OnConflictStrategy.REPLACE)
    fun insertLeaderboardPlayer(players: List<PlayerRankingResponse>): Completable

    @Query("SELECT * FROM ranking_table ORDER BY rank LIMIT 20")
    fun getLeaderboardFromRoon(): Flowable<List<PlayerRankingResponse>>

    @Query("DELETE FROM ranking_table")
    fun deleteLeaderboardData(): Completable

    @Query("DELETE FROM option_table WHERE questionId = :id")
    fun deleteOptionsForQuestionWithId(id: Long): Single<Unit>

    @Query("DELETE FROM option_table")
    fun deleteAllOptions(): Single<Unit>

    @Query("SELECT question_table.questionId, option_table.option_id, option_table.option, question_table.question, question_table.category, question_table.questionNumber FROM question_table JOIN option_table ON question_table.questionId = option_table.questionId WHERE question_table.questionId = :id")
    fun selectParticularQuestionRoom(id: Long): Single<List<CombinedQuestionOptionDataClass>>

    @Query("SELECT question_table.questionId, option_table.option_id, option_table.option, question_table.question, question_table.category, question_table.questionNumber FROM question_table JOIN option_table ON question_table.questionId = option_table.questionId WHERE category = :category ")
    fun selectQuestionsInCategory(category: String): Flowable<List<CombinedQuestionOptionDataClass>>

    @Query("SELECT question_table.questionId, option_table.option_id, option_table.option, question_table.question, question_table.category, question_table.questionNumber FROM question_table JOIN option_table ON question_table.questionId = option_table.questionId")
    fun getAllQuestions(): Flowable<List<CombinedQuestionOptionDataClass>>
}