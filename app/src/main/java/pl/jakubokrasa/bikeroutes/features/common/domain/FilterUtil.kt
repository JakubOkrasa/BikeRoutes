package pl.jakubokrasa.bikeroutes.features.common.domain

// check if the bounding box of a route at least partly covers the bounding box of chosen location
fun doesRouteCoversMap(routeBB: BoundingBoxData, mapBB: BoundingBoxData
) = doesRouteCoversMapVertically(routeBB, mapBB) && doesRouteCoversMapHorizontally(routeBB, mapBB)

private fun doesRouteCoversMapHorizontally(
    routeBB: BoundingBoxData, mapBB: BoundingBoxData
) = doesRouteCoversMapAndBottomIsOutside(routeBB, mapBB) || doesRouteCoversMapAndTopIsOutside(
    routeBB,
    mapBB) || isRouteBetweenVertically(routeBB, mapBB)

private fun doesRouteCoversMapVertically(
    routeBB: BoundingBoxData, mapBB: BoundingBoxData
) = doesRouteCoversMapAndLeftIsOutside(routeBB, mapBB) || doesRouteCoversMapAndRightIsOutside(
    routeBB, mapBB) || isRouteBetweenHorizontally(routeBB, mapBB)


private fun doesRouteCoversMapAndBottomIsOutside(
    routeBB: BoundingBoxData, mapBB: BoundingBoxData
) = (routeBB.latSouth < mapBB.latSouth && routeBB.latNorth > mapBB.latSouth)

private fun doesRouteCoversMapAndTopIsOutside(
    routeBB: BoundingBoxData, mapBB: BoundingBoxData
) = (routeBB.latNorth > mapBB.latNorth && routeBB.latSouth < mapBB.latNorth)

private fun isRouteBetweenVertically(
    routeBB: BoundingBoxData, mapBB: BoundingBoxData
) = (routeBB.latNorth < mapBB.latNorth && routeBB.latSouth > mapBB.latSouth)


private fun doesRouteCoversMapAndLeftIsOutside(
    routeBB: BoundingBoxData, mapBB: BoundingBoxData
) = (routeBB.lonWest < mapBB.lonWest && routeBB.lonEast > mapBB.lonWest)

private fun doesRouteCoversMapAndRightIsOutside(
    routeBB: BoundingBoxData, mapBB: BoundingBoxData
) = (routeBB.lonEast > mapBB.lonEast && routeBB.lonWest < mapBB.lonEast)

private fun isRouteBetweenHorizontally(
    routeBB: BoundingBoxData, mapBB: BoundingBoxData
) = (routeBB.lonEast < mapBB.lonEast && routeBB.lonWest > mapBB.lonWest)