package ro.utcn.sd.icsaszar.assign1.dto

interface ConvertibleToDTO<T> {
    fun toDTO():T
}