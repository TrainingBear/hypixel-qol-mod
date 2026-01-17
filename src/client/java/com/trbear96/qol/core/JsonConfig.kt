package com.trbear96.qol.core

import com.fasterxml.jackson.core.util.DefaultIndenter
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import net.fabricmc.loader.api.FabricLoader
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.nio.file.Files
import java.util.function.Consumer
import kotlin.io.path.absolutePathString

/** @author Drexyz1945
 */
class JsonConfig {
    val parent: File
    private val jsons: MutableMap<String, ObjectNode> = HashMap()
    private val compounds: MutableMap<String, JsonConfig> = HashMap()
    private val isPrettyPrint: Boolean

    constructor(
        path: String?,
        folderName: String?,
        prettyPrint: Boolean,
        autoLoad: Boolean
    ) {
        val realPath = path ?: DEFAULT_PATH.absolutePath
        parent = if (folderName != null) File(realPath, folderName) else File(realPath)
        isPrettyPrint = prettyPrint

        if (!parent.exists()) parent.mkdirs()
        if (autoLoad) load()
    }

    constructor(folderName: String, prettyPrint: Boolean, autoLoad: Boolean) :
            this(null, folderName, prettyPrint, autoLoad)

    constructor(folder: File, prettyPrint: Boolean, autoLoad: Boolean) :
            this(folder.absolutePath, null, prettyPrint, autoLoad)

    fun has(name: String): Boolean = jsons.containsKey(name)

    fun load() {
        val files = parent.listFiles() ?: return
        try {
            for (file in files) {
                if (file.isDirectory) {
                    val config = JsonConfig(file, isPrettyPrint, true)
                    config.load()
                    compounds[file.name] = config
                } else if (file.name.endsWith(".json")) {
                    jsons[file.name] =
                        jsonHandler.readTree(file) as ObjectNode
                }
            }
            println("[JsonConfig] Loaded ${parent.name}")
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    @Deprecated("Use makeOrGetCompound for safety")
    fun getCompound(name: String): JsonConfig? {
        require(name.isNotEmpty()) { "The compound cannot be empty!" }
        return compounds[name]
    }

    fun makeOrGetCompound(name: String): JsonConfig {
        return compounds[name] ?: JsonConfig(parent.absolutePath, name, isPrettyPrint, true).also {
            compounds[name] = it
        }
    }

    fun deleteCompound(name: String): JsonConfig {
        require(name.isNotEmpty()) { "The compound cannot be empty!" }
        val folder = File(parent, name)
        if (folder.exists()) folder.delete()
        compounds.remove(name)
        return this
    }

    fun entry(): Map<String, JsonConfig> = compounds

    fun createOrGet(nameFile: String): ObjectNode? {
        require(nameFile.isNotEmpty()) { "The .json cannot be empty!" }
        val fileName = if (nameFile.endsWith(".json")) nameFile else "$nameFile.json"
        val file = File(parent, fileName)

        if (file.exists()) return get(fileName)

        return try {
            file.createNewFile()
            FileWriter(file).use { it.write("{}") }
            val obj = jsonHandler.readTree(String(Files.readAllBytes(file.toPath()))) as ObjectNode
            jsons[fileName] = obj
            println("success create json file $fileName")
            obj
        } catch (e: Exception) {
            println("$fileName cant create: ${e.message}")
            null
        }
    }

    fun deleteFile(filename: String, deleteOnMap: Boolean): ObjectNode? {
        require(filename.isNotEmpty()) { "The .json cannot be empty!" }
        val name = if (filename.endsWith(".json")) filename else "$filename.json"
        val file = File(parent, name)
        if (file.exists()) file.delete()
        return if (deleteOnMap) jsons.remove(name) else null
    }

    fun save(jsonName: String) {
        require(jsonName.isNotEmpty()) { "The .json cannot be empty!" }
        val name = if (jsonName.endsWith(".json")) jsonName else "$jsonName.json"
        val obj = jsons[name] ?: return

        try {
            val file = File(parent, name)
            if (isPrettyPrint)
                jsonHandler.writer(prettyWriter).writeValue(file, obj)
            else
                jsonHandler.writeValue(file, obj)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun saveAll() {
        for ((key, value) in jsons) {
            try {
                val file = File(parent, key)
                if (isPrettyPrint)
                    jsonHandler.writer(prettyWriter).writeValue(file, value)
                else
                    jsonHandler.writeValue(file, value)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun get(jsonName: String): ObjectNode? {
        require(jsonName.isNotEmpty()) { "The .json name cannot be empty!" }
        val name = if (jsonName.endsWith(".json")) jsonName else "$jsonName.json"
        return jsons[name]
    }

    fun getJsonsMap(): Map<String, ObjectNode> = jsons

    fun edit(filename: String, autoSave: Boolean, editor: Consumer<ObjectNode>) {
        val obj = get(filename) ?: throw NullPointerException("File $filename does not exist")
        editor.accept(obj)
        if (autoSave) save(filename)
    }

    companion object {
        val DEFAULT_PATH: File = File(FabricLoader.getInstance().configDir.absolutePathString())
        val jsonHandler: ObjectMapper = ObjectMapper()

        private val prettyWriter = DefaultPrettyPrinter().apply {
            val indenter = DefaultIndenter("  ", "\n")
            indentObjectsWith(indenter)
            indentArraysWith(indenter)
        }

        fun ObjectNode.getOrMakeArray(key: String): ArrayNode {
            val node = this.get(key)
            return if (node != null && node.isArray) node as ArrayNode
            else jsonHandler.createArrayNode().also { this.set<ArrayNode>(key, it) }
        }

        fun ObjectNode.getOrMakeObject(key: String): ObjectNode {
            val node = this.get(key)
            return if (node != null && node.isObject) node as ObjectNode
            else jsonHandler.createObjectNode().also { this.set<ObjectNode>(key, it) }
        }

        fun ArrayNode.getStringArray(): Array<String> =
            Array(this.size()) { this[it].asText() }

        fun ArrayNode.getIntArray(): IntArray =
            IntArray(this.size()) { this[it].asInt() }

        fun ArrayNode.getDoubleArray(): DoubleArray =
            DoubleArray(this.size()) { this[it].asDouble() }

        fun ArrayNode.delStrArray(strToRemove: String): ArrayNode {
            val newArr = jsonHandler.createArrayNode()
            for (element in this)
                if (element.toString() != strToRemove)
                    newArr.add(element)
            return newArr
        }

        fun createArray(): ArrayNode = jsonHandler.createArrayNode()
        fun createObject(): ObjectNode = jsonHandler.createObjectNode()
    }
}