package com.dvm.appd.oasis.dbg.elas.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dvm.appd.oasis.dbg.R
import com.dvm.appd.oasis.dbg.elas.model.retrofit.PlayerRankingResponse
import kotlinx.android.synthetic.main.card_leaderboard_elas.view.*

class ELasLeaderoardAdapter(val context: Context) : RecyclerView.Adapter<ELasLeaderoardAdapter.ElasLeaderboardViewHolder>() {

    var leaderboardList: List<PlayerRankingResponse> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElasLeaderboardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_leaderboard_elas, parent, false)
        return ElasLeaderboardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return leaderboardList.size
    }

    override fun onBindViewHolder(holder: ElasLeaderboardViewHolder, position: Int) {
        holder.playerName.text = leaderboardList[position].Name
        holder.playerPoints.text = "${leaderboardList[position].Points.toString()} Pts"
        holder.textRank.text = leaderboardList[position].Rank.toString()
        when(leaderboardList[position].Rank) {
            1 -> {
                holder.imgRank.setImageDrawable(context.getDrawable(R.drawable.ic_gold_cup))
            }
            2 -> {
                holder.imgRank.setImageDrawable(context.getDrawable(R.drawable.ic_silver_cup))
            }
            3 -> {
                holder.imgRank.setImageDrawable(context.getDrawable(R.drawable.ic_bronze_cup))
            }
            else -> {
                holder.imgRank.setImageDrawable(context.getDrawable(R.drawable.ic_normal_cup))
            }
        }
    }

    inner class ElasLeaderboardViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val imgRank = view.img_leaderboard_ranking
        val playerPoints = view.text_leaderboard_points
        val playerName = view.text_leaderboard_name
        val textRank = view.text_elasLeaderboard_rank
    }

}