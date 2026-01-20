<template>
    <div class="flex-1">
        <h2 class="text-2xl font-bold text-center m-5">Issue report</h2>
        <DataView :value="processedIssues" responsiveLayout="scroll" paginator :rows="10" :rowsPerPageOptions="[10, 20, 50]" class="mx-5 my-3">
            <template #list="slotProps">
                <div class="flex flex-col divide-y divide-gray-500">
                    <div v-for="(item, index) in slotProps.items" :key="index" class="p-2">
                        <div class="flex items-center gap-2">
                            <div v-html="getIcon(item.component).svg" class="w-5 h-5"></div>
                            <div class="flex flex-col">
                                <span>{{ item.file }}</span>
                                <span class="text-xs text-gray-500">{{ item.location }}</span>
                            </div>
                        </div>
                        <div>
                            <a :href="item.ruleDocLink" target="_blank">{{ item.rule }}</a>
                            <Chip v-for="tag in item.tags" :key="tag" :label="tag" class="ml-2"/>
                        </div>
                        <div>
                            {{ item.message }}
                        </div>
                        <div class="mt-2 mb-1">
                            <Chip class="m-1" v-if="item.severity" :label="item.severity" />
                            <Chip class="m-1" v-if="item.type" :label="item.type" />
                            <Chip class="m-1" v-if="item.impacts.softwareQuality" :label="item.impacts.softwareQuality" />
                            <Chip class="m-1" v-if="item.impacts.severity" :label="item.impacts.severity" />
                            <Chip v-if="item.quickFixAvailable" class="m-1" label="QF" />
                            <Chip class="m-1" v-if="item.effort" :label="item.effort" />
                        </div>
                    </div>
                </div>
            </template>
        </DataView>
    </div>
</template>

<script setup>
import { getIcon } from 'material-file-icons';

import { SonarQubeIssueMapper } from '../mappers/sonar-qube-issue.mapper';
import { SemgrepIssueMapper } from '../mappers/semgrep-issue.mapper';

const { issues } = defineProps({
    issues: {
        type: Array,
        required: true
    }
})

const processedIssues = computed(() => {
    return issues.map(SemgrepIssueMapper.mapIssue);
});
</script>