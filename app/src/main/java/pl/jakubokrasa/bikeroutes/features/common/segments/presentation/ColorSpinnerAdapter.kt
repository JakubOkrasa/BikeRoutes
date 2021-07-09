package pl.jakubokrasa.bikeroutes.features.common.segments.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import pl.jakubokrasa.bikeroutes.R


class ColorSpinnerAdapter(
    context: Context,
    private val resource: Int,
    private val colorsRes: Int,
    colorNames: Array<String>
): ArrayAdapter<String>(context, resource, R.id.color_spinner_tv, colorNames) {

    private var layoutInflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {


        val holder: ViewHolder
        val retView: View


        if(convertView == null){
            retView = layoutInflater.inflate(resource, null)
            holder = ViewHolder()

            holder.textView = retView.findViewById(R.id.color_spinner_tv) as TextView
            retView.tag = holder

        } else {
            holder = convertView.tag as ViewHolder

            val segmentColors = context.resources.getIntArray(colorsRes)
            holder.textView?.setBackgroundColor(segmentColors[position])
            holder.textView?.text = "Text $position"

            retView = convertView
        }

        val segmentColors = context.resources.getIntArray(colorsRes)
        holder.textView?.setBackgroundColor(segmentColors[position])
        holder.textView?.text = "Text $position"
//        holder.textView?.text = segmentColors[position].toString()
//        retView.setBackgroundColor(R.color.purple_200)

        return retView
    }

    internal class ViewHolder {
        var textView: TextView? = null
    }
}