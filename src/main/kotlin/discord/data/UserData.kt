package kito.metapolemika.discord.data

import dev.kord.common.entity.Snowflake
import dev.kord.core.entity.User
import kito.metapolemika.core.form.FormBase

private val userActualForms = HashMap<Snowflake, ArrayList<FormBase<*>>>()

var User.forms : ArrayList<FormBase<*>>
    get() = userActualForms.computeIfAbsent(id) { arrayListOf() }
    set(value) { userActualForms[id] = value }