package sample.wasmApp

import kotlinx.interop.wasm.dom.*
import kotlinx.wasm.jsinterop.*

fun main() {

    val myCanvas = document.getElementById("myCanvas").asCanvas
    val ctx = myCanvas.getContext("2d")
    val rect = myCanvas.getBoundingClientRect()
//    val rectLeft = rect.left
//    val rectTop = rect.top

    println(rect.right)
    println(rect.bottom)

    val magnificationFactor = 200.0
    val panX = 2.0
    val panY = 1.5

    for (x in 0..rect.right) {
        for (y in 0..rect.bottom) {
            val belongsToSet =
                checkIfBelongsToMandelbrotSet(
                    x / magnificationFactor - panX,
                    y / magnificationFactor - panY
                )
            if(belongsToSet == 0.0) {
                ctx.fillStyle = "#000"
                ctx.fillRect(x,y, 1,1) // Draw a black pixel
            } else {
                ctx.fillStyle = "hsl(0, 100%, $belongsToSet%)"
                ctx.fillRect(x,y, 1,1) // Draw a colorful pixel
            }
        }
    }


//    var mouseX: Int = 0
//    var mouseY: Int = 0
//    var draw: Boolean = false

//    document.setter("onmousemove") { arguments: ArrayList<JsValue> ->
//        val event = MouseEvent(arguments[0])
//        mouseX = event.getInt("clientX") - rectLeft
//        mouseY = event.getInt("clientY") - rectTop
//
//        if (mouseX < 0) mouseX = 0
//        if (mouseX > 639) mouseX = 639
//        if (mouseY < 0) mouseY = 0
//        if (mouseY > 479) mouseY = 479
//    }

//    document.setter("onmousedown") {
//        draw = true
//    }
//
//    document.setter("onmouseup") {
//        draw = false
//    }

//    setInterval(10) {
//        if (draw) {
//            ctx.strokeStyle = "#222222"
//            ctx.lineTo(mouseX, mouseY)
//            ctx.stroke()
//        } else {
//            ctx.moveTo(mouseX, mouseY)
//            ctx.stroke()
//        }
//    }
}

fun checkIfBelongsToMandelbrotSet(x: Double, y: Double): Double {
    var realComponentOfResult = x;
    var imaginaryComponentOfResult = y;
    var maxIterations = 100;
    for (i in 0..maxIterations) {
        // Calculate the real and imaginary components of the result
        // separately
        val tempRealComponent = realComponentOfResult * realComponentOfResult-imaginaryComponentOfResult * imaginaryComponentOfResult +x

        val tempImaginaryComponent = 2 * realComponentOfResult * imaginaryComponentOfResult +y

        realComponentOfResult = tempRealComponent
        imaginaryComponentOfResult = tempImaginaryComponent

        if(realComponentOfResult * imaginaryComponentOfResult > 5)
            return (i.toDouble()/maxIterations.toDouble() * 100.0) // Return a number as a percentage
    }


    return 0.0; // Not in the set
}

