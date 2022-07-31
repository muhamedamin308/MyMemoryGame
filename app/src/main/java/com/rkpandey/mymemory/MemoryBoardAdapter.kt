package com.rkpandey.mymemory

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.rkpandey.mymemory.models.BoardSize
import com.rkpandey.mymemory.models.MemoryCard
import kotlin.math.min

class MemoryBoardAdapter(
    private val context: Context,
    private val numPieces: BoardSize,
    private val cards: List<MemoryCard>,
    private val cardClickListener: CardClickListener
) :
    RecyclerView.Adapter<MemoryBoardAdapter.ViewHolder>() {

    companion object {
        private const val MARGIN_SIZE = 10
        private const val TAG = "MemoryBoardAdapter"
    }

    interface CardClickListener{
        fun onCardClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cardWidth : Int = parent.width / numPieces.getWidth() - (2 * MARGIN_SIZE)
        val cardHeight : Int = parent.height / numPieces.getHeight() - (2 * MARGIN_SIZE)
        val cardSideLength : Int = min(cardWidth,cardHeight)
        val view = LayoutInflater.from(context).inflate(R.layout.memory_card, parent, false)
        val layoutParams = view.findViewById<CardView>(R.id.cardView).layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.width = cardSideLength
        layoutParams.height = cardSideLength
        layoutParams.setMargins(MARGIN_SIZE,MARGIN_SIZE,MARGIN_SIZE,MARGIN_SIZE )
        return ViewHolder(view)
    }

    override fun getItemCount() = numPieces.numCards

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        private val imageButton : ImageView = itemView.findViewById(R.id.imageButton)

        fun bind(position: Int){
            val memoryCard =cards[position]
            imageButton.setImageResource(if (memoryCard.isFaceUp) memoryCard.identifier else R.drawable.ic_launcher_background)
            imageButton.alpha = if (memoryCard.isMatched).4f else 1.0f
            val colorStateList =  if (memoryCard.isMatched) ContextCompat.getColorStateList(context,R.color.color_gray) else null
            ViewCompat.setBackgroundTintList(imageButton, colorStateList)
            imageButton.setOnClickListener {
                Log.i(TAG, "Clicked On the Position $position")
                cardClickListener.onCardClick(position)
            }
        }
    }
}
