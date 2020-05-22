package processor
import java.lang.Exception
import java.util.Scanner
import kotlin.math.pow

val scanner = Scanner(System.`in`)

class InvalidMatrixOperationException(message: String): Exception(message)

class Matrix(private val matrix: Array<DoubleArray>){
    val rows = matrix.size
    val cols = matrix[0].size

    operator fun plus(other: Matrix): Matrix {
        if (this.rows != other.rows || this.cols != other.cols) {
            throw InvalidMatrixOperationException("Error: matrices must of same size")
        } else {
            val resultMatrix = Matrix(Array(rows) {DoubleArray(cols)})
            for (i in 0 until rows){
                for (j in 0 until cols){
                    resultMatrix[i][j] = this[i][j] + other[i][j]
                }
            }
            return resultMatrix
        }
    }

    operator fun times(other: Any): Matrix {
        val resultMatrix: Matrix
        when (other) {
            is Int -> {
                resultMatrix = Matrix(Array(rows) { DoubleArray(cols) })
                for (i in 0 until rows) {
                    for (j in 0 until cols) {
                        resultMatrix[i][j] = this[i][j] * other
                    }
                }
                return resultMatrix
            }
            is Matrix -> {
                if (this.cols != other.rows) {
                    throw InvalidMatrixOperationException("Error: number of columns of the 1st matrix must be same as number of rows of 2nd matrix")
                }
                resultMatrix = Matrix(Array(this.rows) { DoubleArray(other.cols) })
                for (i in 0 until this.rows) {
                    for (j in 0 until other.cols) {
                        for (k in 0 until other.rows) {
                            resultMatrix[i][j] += this[i][k] * other[k][j]
                        }
                    }
                }
                return resultMatrix
            }
            else -> {
                throw InvalidMatrixOperationException("Error: multiplier must be either an Int or Matrix object")
            }
        }
    }

    fun determinant(): Double {
        if (rows != cols){
            throw InvalidMatrixOperationException("Error: not a square matrix")
        } else if (rows == 2) return this[0][0] * this[1][1] - this[1][0] * this[0][1]
        else {
            var det = 0.0
            for (k in 0 until cols) {
                val minor = Matrix(Array(cols - 1) { DoubleArray(rows - 1) })
                var t = 0
                for (i in 1 until rows) {
                    var p = 0
                    for (j in 0 until cols) {
                        if (j == k) continue
                        else {
                            minor[t][p] = this[i][j]
                            p += 1
                        }
                    }
                    t += 1
                }
                det += this[0][k] * (-1.0).pow((k + 2).toDouble()) * minor.determinant()
            }
            return det
        }
    }

    fun transposeMainDiagonal(): Matrix {
        val resultMatrix = Matrix(Array(cols) { DoubleArray(rows) })
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                resultMatrix[j][i] = this[i][j]
            }
        }
        return resultMatrix
    }

    fun transposeSideDiagonal(): Matrix {
        val resultMatrix = Matrix(Array(cols) { DoubleArray(rows) })
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                resultMatrix[i][j] = this[cols - j - 1][rows - i - 1]
            }
        }
        return resultMatrix
    }

    fun transposeVertical(): Matrix {
        val resultMatrix = Matrix(Array(rows) { DoubleArray(cols) })
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                resultMatrix[i][j] = this[i][rows - j - 1]
            }
        }
        return resultMatrix
    }

    fun transposeHorizontal(): Matrix {
        val resultMatrix = Matrix(Array(cols) { DoubleArray(rows) })
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                resultMatrix[i][j] = this[cols - i - 1][j]
            }
        }
        return resultMatrix
    }

    operator fun get(i: Int): DoubleArray = matrix[i]

    override fun toString(): String = matrix.joinToString("\n") {
        it.joinToString(" ")
    }
}

fun readMatrix(n: Int, m: Int): Matrix {
    val matrix = Matrix(Array(n) {DoubleArray(m)})

    for (i in 0 until n) {
        for (j in 0 until m) {
            if (scanner.hasNextInt()) {
                val item = scanner.nextInt()
                matrix[i][j] = item.toDouble()
            } else if (scanner.hasNextDouble()) {
                val item = scanner.nextDouble()
                matrix[i][j] = item
            }
        }
        scanner.nextLine()
    }

    return matrix
}

fun addMatrices(): Matrix {
    println("Enter size of first matrix: ")
    var rows = scanner.nextInt()
    var cols = scanner.nextInt()
    val a = readMatrix(rows, cols)
    println("Enter size of second matrix: ")
    rows = scanner.nextInt()
    cols = scanner.nextInt()
    val b = readMatrix(rows, cols)
    println("The addition result is:\n")
    return a + b
}

fun scalarMult(): Matrix {
    println("Enter size of matrix: ")
    val rows = scanner.nextInt()
    val cols = scanner.nextInt()
    val matrix = readMatrix(rows, cols)
    print("Enter constant: ")
    val scalar = scanner.nextInt()
    print("The multiplication result is:\n")
    return matrix * scalar
}

fun matrixMult(): Matrix {
    println("Enter size of first matrix: ")
    var rows = scanner.nextInt()
    var cols = scanner.nextInt()
    val a = readMatrix(rows, cols)
    println("Enter size of second matrix: ")
    rows = scanner.nextInt()
    cols = scanner.nextInt()
    val b = readMatrix(rows, cols)
    println("The multiplication result is:\n")
    return a * b
}

fun transpose(): Matrix {
    println("1. Main diagonal\n" +
            "2. Side diagonal\n" +
            "3. Vertical line\n" +
            "4. Horizontal line")
    println("Your choice: ")
    val choice = scanner.nextInt()
    println("Enter size of matrix: ")
    val rows = scanner.nextInt()
    val cols = scanner.nextInt()
    val matrix = readMatrix(rows, cols)
    return when(choice) {
        1 -> matrix.transposeMainDiagonal()
        2 -> matrix.transposeSideDiagonal()
        3 -> matrix.transposeVertical()
        4 -> matrix.transposeHorizontal()
        else -> matrix
    }
}

fun determinant(): Double {
    println("Enter size of matrix: ")
    val rows = scanner.nextInt()
    val cols = scanner.nextInt()
    val matrix = readMatrix(rows, cols)
    return matrix.determinant()
}

fun main() {
    loop@ while (true) {
        println("1. Add matrices\n" +
                "2. Multiply matrix to a constant\n" +
                "3. Multiply matrices\n" +
                "4. Transpose matrix\n" +
                "5. Calculate a determinant\n" +
                "0. Exit")
        println("Your choice: ")
        try {
            when (scanner.nextInt()) {
                1 -> println(addMatrices())
                2 -> println(scalarMult())
                3 -> println(matrixMult())
                4 -> println(transpose())
                5 -> println(determinant())
                0 -> break@loop
                else -> continue@loop
            }
        } catch (e: InvalidMatrixOperationException) {
            println(e.message)
        }
    }
}
