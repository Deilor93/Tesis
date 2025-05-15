import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.tesis.vista.modelo.DraggableItem
import com.example.tesis.vista.modelo.GridCell
import kotlin.math.hypot
import kotlin.math.roundToInt

@Composable
fun ImagenArrastrable(
    item: DraggableItem,
    gridCells: List<GridCell>,
    initialOffset: Offset
) {
    var offset by remember { mutableStateOf(initialOffset) }

    Box(
        modifier = Modifier
            .offset { IntOffset(offset.x.roundToInt(), offset.y.roundToInt()) }
            .size(80.dp)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume()
                        offset += dragAmount
                    },
                    onDragEnd = {
                        // Snap to nearest cell
                        val nearestCell = gridCells.minByOrNull { cell ->
                            hypot(
                                cell.position.x - offset.x - 40f,
                                cell.position.y - offset.y - 40f
                            )
                        }
                        nearestCell?.let {
                            item.assignedCell = it
                            offset = Offset(it.position.x - 40f, it.position.y - 40f)
                        }
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = item.imageResId),
            contentDescription = item.id,
            modifier = Modifier.fillMaxSize()
        )
    }
}