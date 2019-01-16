package kr.co.tmon.simplex


interface ILogger {
	//fun write (message: String)

	val write: Function1<String, Unit>
}

