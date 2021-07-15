package pl.jakubokrasa.bikeroutes.features.myroutes.presentation
//
//import android.R
//import android.content.Context.LAYOUT_INFLATER_SERVICE
//import android.view.LayoutInflater
//import android.widget.LinearLayout
//import androidx.core.content.ContextCompat.getSystemService
//import org.osmdroid.views.MapView
//import org.osmdroid.views.overlay.infowindow.InfoWindow
//
//
//class SegmentInfoWindow(
//    layoutResId: Int, mapView: MapView
//): InfoWindow(layoutResId, mapView) {
//    override fun onOpen(item: Any?) {
//        val l = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater?
//        val myRoot = LinearLayout(getBaseContext())
//        val v: View = l.inflate(R.layout., myRoot, false)
//    }
//
//
//    override fun onClose() {
//        TODO("Not yet implemented")
//    }
//}