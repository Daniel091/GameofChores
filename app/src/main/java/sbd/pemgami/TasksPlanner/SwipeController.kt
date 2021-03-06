package sbd.pemgami.TasksPlanner

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper.*
import android.view.View
import sbd.pemgami.R

class SwipeController(private val actions: SwipeControllerActions, context: Context) : Callback() {
    private val swipeWidth = 50
    private val background = ColorDrawable()
    private val redColor = Color.parseColor("#f44336")
    private val greenColor = Color.parseColor("#0F9D58")
    private val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }

    private val deleteIcon = ContextCompat.getDrawable(context, R.drawable.ic_delete)
    private val checkIcon = ContextCompat.getDrawable(context, R.drawable.ic_done)

    override fun onChildDraw(c: Canvas?, recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        drawFeedback(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun drawFeedback(c: Canvas?, recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        val itemView = viewHolder?.itemView ?: return
        val isCanceled = dX == 0f && !isCurrentlyActive

        if (isCanceled) {
            clearCanvas(c, itemView.right + dX, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat())
            return
        }

        // decide which side to show red or green
        var red = false
        var green = false
        when {
            dX < -swipeWidth -> red = true
            dX > swipeWidth -> green = true
            else -> return
        }

        if (red) {
            showCancelFeedback(dX, itemView, c)
        } else if (green) {
            showSuccessFeedback(dX, itemView, c)
        }
    }

    private fun showSuccessFeedback(dX: Float, itemView: View, c: Canvas?) {
        background.color = greenColor
        background.setBounds(itemView.left + dX.toInt(), itemView.top, itemView.left, itemView.bottom)
        background.draw(c)

        checkIcon?.let {
            val itemHeight = itemView.bottom - itemView.top
            val intrinsicWidth = checkIcon.intrinsicWidth
            val intrinsicHeight = checkIcon.intrinsicHeight
            val checkIconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
            val checkIconMargin = (itemHeight - intrinsicHeight) / 2
            val checkIconLeft = itemView.left + checkIconMargin - intrinsicWidth + 40
            val checkIconRight = itemView.left + intrinsicHeight + 40
            val checkIconBottom = checkIconTop + intrinsicHeight
            checkIcon.setBounds(checkIconLeft, checkIconTop, checkIconRight, checkIconBottom)
            checkIcon.draw(c)
        }
    }

    private fun showCancelFeedback(dX: Float, itemView: View, c: Canvas?) {
        background.color = redColor
        background.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
        background.draw(c)

        deleteIcon?.let {
            val itemHeight = itemView.bottom - itemView.top
            val deleteIconTop = itemView.top + (itemHeight - deleteIcon.intrinsicHeight) / 2
            val deleteIconMargin = (itemHeight - deleteIcon.intrinsicHeight) / 2
            val deleteIconLeft = itemView.right - deleteIconMargin - deleteIcon.intrinsicWidth
            val deleteIconRight = itemView.right - deleteIconMargin
            val deleteIconBottom = deleteIconTop + deleteIcon.intrinsicHeight

            deleteIcon.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
            deleteIcon.draw(c)
        }
    }

    private fun clearCanvas(c: Canvas?, left: Float, top: Float, right: Float, bottom: Float) {
        c?.drawRect(left, top, right, bottom, clearPaint)
    }

    override fun getMovementFlags(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?): Int {
        return makeMovementFlags(0, LEFT or RIGHT)
    }

    override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
        when (direction) {
            4 -> actions.onLeftSwipe(viewHolder?.adapterPosition)
            8 -> actions.onRightSwipe(viewHolder?.adapterPosition)
        }
    }
}