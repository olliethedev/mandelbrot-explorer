package sample.wasmApp

import kotlinx.interop.wasm.dom.*
import kotlinx.wasm.jsinterop.*

fun main() {
    val myCanvas = document.getElementById("myCanvas").asCanvas
    val ctx = myCanvas.getContext("2d")
    val rect = myCanvas.getBoundingClientRect()

    println("w:${rect.right} h:${rect.bottom}")

    var magnificationFactor = 200.0
    val panX = 2.0
    val panY = 1.5
    var hue = 200;

    drawSet(ctx, rect.right, rect.bottom, magnificationFactor, panX, panY, hue)

    myCanvas.setter("onclick") { arguments: ArrayList<JsValue> ->
        val event = MouseEvent(arguments[0])
        val mouseX = event.getInt("clientX")
        val mouseY = event.getInt("clientY")
        val mouseXPercent = mouseX.toDouble() / rect.right.toDouble()
        val mouseYPercent = mouseY.toDouble() / rect.bottom.toDouble()
        println("$mouseX,$mouseY")
        println("$mouseXPercent,$mouseYPercent")
        magnificationFactor += 100
        drawSet(ctx, rect.right, rect.bottom, magnificationFactor, 1 + panX * (1.0 - mouseXPercent), 1 + panY * (1.0 - mouseYPercent), hue)
    }
}

inline fun drawSet(ctx: Context, width: Int, height: Int, magnificationFactor: Double, panX: Double, panY: Double, hue: Int) {
    for (x in 0..width) {
        for (y in 0..height) {
            val belongsToSet =
                checkIfBelongsToMandelbrotSet(
                    x / magnificationFactor - panX,
                    y / magnificationFactor - panY
                )
            if (belongsToSet == 0.0) {
                ctx.fillStyle = "#000"
                ctx.fillRect(x, y, 1, 1) // Draw a black pixel
            } else {
                ctx.fillStyle = "hsl($hue, 100%, $belongsToSet%)"
                ctx.fillRect(x, y, 1, 1) // Draw a colorful pixel
            }
        }
    }
}

inline fun checkIfBelongsToMandelbrotSet(x: Double, y: Double): Double {
    var realComponentOfResult = x
    var imaginaryComponentOfResult = y
    var maxIterations = 90
    for (i in 0..maxIterations) {
        // Calculate the real and imaginary components of the result
        // separately
        val tempRealComponent = realComponentOfResult * realComponentOfResult - imaginaryComponentOfResult * imaginaryComponentOfResult + x

        val tempImaginaryComponent = 2 * realComponentOfResult * imaginaryComponentOfResult + y

        realComponentOfResult = tempRealComponent
        imaginaryComponentOfResult = tempImaginaryComponent

        if (realComponentOfResult * imaginaryComponentOfResult > 5)
            return (i.toDouble() / maxIterations.toDouble() * 100.0) // Return a number as a percentage
    }

    return 0.0 // Not in the set
}

