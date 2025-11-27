package com.example.gamezone

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gamezone.models.Game
import com.squareup.picasso.Picasso

class GamesAdapter(private val games: List<Game>) : RecyclerView.Adapter<GamesAdapter.GameViewHolder>() {

    class GameViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val container: View = view
        val title: TextView = view.findViewById(R.id.tvGameTitle)
        val description: TextView = view.findViewById(R.id.tvGameDescription)
        val thumbnail: ImageView = view.findViewById(R.id.ivGameThumbnail)
        val genre: TextView = view.findViewById(R.id.tvGameGenre)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_game, parent, false)
        return GameViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val game = games[position]
        holder.title.text = game.title
        holder.description.text = game.short_description
        holder.genre.text = "${game.genre} | ${game.platform}"

        // Cargar imagen
        if (game.thumbnail.isNotEmpty()) {
            Picasso.get()
                .load(game.thumbnail)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_delete)
                .into(holder.thumbnail)
        }

        // Evento Click: Abrir Detalles
        holder.container.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, GameDetailActivity::class.java)
            intent.putExtra("GAME_DATA", game)
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = games.size
}