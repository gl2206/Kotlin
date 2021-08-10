import kotlin.random.*
import kotlin.concurrent.*

val NB=2

class Bal: Object() {
    private var msg: String = ""

    fun lire(): String { synchronized(this) {
        while(msg == "")
            wait()
        var t = msg
        msg = ""      
        notifyAll()
        return t
        }
    }

    fun ecrire(m: String) {synchronized(this) {   
    while(msg != "")
            wait()
        this.msg = m
        notifyAll()
        }}
}

class Producteur(val name: String, private val b: Bal) {
    fun deposerMsg(m: String) { this.b.ecrire(m) }
    fun start() {
        var cpt=0
        repeat(NB) {
            Thread.sleep(Random.nextLong(10,100))
            cpt++
            this.b.ecrire("msg n°"+cpt+" from "+this.name)
        }
    }
}

class Consommateur(val name: String, private val b: Bal) {
    fun recupererMsg(): String = b.lire()
    fun start() {
        var cpt=0
        repeat(NB) {
            Thread.sleep(Random.nextLong(10,100))
            cpt++
            var msg = this.b.lire()
            println("msg n°"+cpt+" lu par "+this.name+" : "+ msg)
        }
    }
}

fun main() {
    var b=Bal()

//    runBlocking {
        for(i in 1..5) {
            thread {
                Producteur("P"+i, b).start()
            }
            thread {
                Consommateur("C"+i, b).start()
            }
        }
}
