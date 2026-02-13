<template>
  <div v-if="pending">Loading...</div>
  <div v-else-if="error">Error: {{ error }}</div>
  <DataView
    v-else
    :value="data"
    responsiveLayout="scroll"
    paginator
    :rows="10"
    :rowsPerPageOptions="[10, 20, 50]"
    class="mx-auto my-3 max-w-3xl"
  >
    <template #list="slotProps">
      <div class="flex flex-col divide-y divide-gray-500">
        <div v-for="report in slotProps.items" :key="report.date" class="p-4">
          <h2
            class="text-xl font-bold mb-4 cursor-pointer hover:text-blue-500 transition-colors"
            @click="
              () =>
                navigateTo(
                  `/projects/${name.toLowerCase()}/${tool.toLowerCase()}/audits/${report.id}`
                )
            "
          >
            {{ new Date(report.date).toLocaleDateString() }}
            {{ new Date(report.date).toLocaleTimeString() }}
          </h2>
          <p>Branch: {{ report.branch }}</p>
          <p>Commit hash: {{ report.commitHash }}</p>
          <p>Triggered by: {{ report.triggeredBy }}</p>
        </div>
      </div>
    </template>
  </DataView>
</template>

<script setup>
const route = useRoute()
const name = route.params.name
const tool = route.params.tool

const { data, pending, error } = useFetch(
  `/api/projects/${name.toLowerCase()}/${tool.toLowerCase()}/audits`
)
</script>
