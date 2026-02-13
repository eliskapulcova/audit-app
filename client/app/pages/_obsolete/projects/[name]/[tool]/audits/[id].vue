<template>
  <div v-if="pending">Loading...</div>
  <div v-else-if="error">Error: {{ error }}</div>
  <div v-else>
    <AuditReport :report="data" :project-name="name" :is-latest="false" />
  </div>
</template>

<script setup>
const route = useRoute()

const name = route.params.name
const tool = route.params.tool
const id = route.params.id

const { data, pending, error } = useFetch(
  `/api/projects/${name}/${tool}/audits/${id}`
)
</script>
