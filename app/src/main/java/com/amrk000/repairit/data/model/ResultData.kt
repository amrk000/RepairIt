package com.amrk000.repairit.data.model

data class ResultData(
    val name: String,
    val brand: String,
    val model: String,
    val mainSpecs: String,
    val problem: String,
    val tools: List<String>,
    val steps: List<String>
){
    override fun toString(): String {
        return  "Repair it App :\n" +
                "Item: $name\n" +
                "Brand: $brand\n" +
                "Model: $model\n" +
                "Main Specs: $mainSpecs\n" +
                "Problem: $problem\n" +
                "Needed Tools: ${tools.joinToString(", ")}\n" +
                "Repair Steps: ${steps.joinToString("\n")}"

    }
}