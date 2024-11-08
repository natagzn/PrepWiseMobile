package com.example.prepwise.models

import com.example.prepwise.SelectableItem
import java.io.Serializable

class Category( override val id: Int, override val name: String): SelectableItem, Serializable
