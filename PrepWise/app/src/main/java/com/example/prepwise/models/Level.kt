package com.example.prepwise.models

import com.example.prepwise.SelectableItem
import java.io.Serializable

class Level( override val id: Int, override val name: String): SelectableItem, Serializable
