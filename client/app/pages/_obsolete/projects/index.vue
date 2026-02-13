<template>
  <div class="px-5">
    <h1 class="text-3xl font-bold text-center m-5 mx-auto">Reports</h1>
    <p v-if="pending">Loading...</p>
    <p v-else-if="error">Error: {{ error }}</p>
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
          <div
            v-for="report in slotProps.items"
            :key="report.projectName"
            class="p-4"
          >
            <h2
              class="text-xl font-bold mb-4 cursor-pointer hover:text-blue-500 transition-colors"
              @click="() => navigateTo(`/reports/${report.projectName}`)"
            >
              {{ report.projectName }}
            </h2>
            <Chip
              v-for="availableReport in report.availableReports"
              :key="availableReport.reportTool"
              :label="availableReport.reportTool"
              class="mr-2 mb-2 cursor-pointer hover:text-blue-500 transition-colors"
              @click="
                () =>
                  navigateTo(
                    `/projects/${report.projectName.toLowerCase()}/${availableReport.reportTool.toLowerCase()}/audits/latest`
                  )
              "
            />
          </div>
        </div>
      </template>
    </DataView>
  </div>
</template>

<script setup lang="ts">
const { data, pending, error } = useFetch('/api/reports')
</script>
