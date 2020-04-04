package fr.lewon.bot.stocks.rest.query

class QueryParametersBuilder {

    private val params: MutableList<Pair<String, String>> = ArrayList()

    fun addParam(name: String, value: String): QueryParametersBuilder {
        params.add(Pair(name, value))
        return this
    }

    fun addParamIfAbsent(name: String, value: String): QueryParametersBuilder {
        if (params.none { it.first == name }) {
            addParam(name, value)
        }
        return this
    }

    fun build(): String {
        return if (params.isEmpty()) ""
        else "?" + params.joinToString("&") { "${it.first}=${it.second}" }
    }

}