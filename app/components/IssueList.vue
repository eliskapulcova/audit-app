<template>
    <DataView :value="processedIssues" responsiveLayout="scroll" paginator :rows="10" :rowsPerPageOptions="[10, 20, 50]" class="mx-5">
        <template #list="slotProps">
            <div class="flex flex-col">
                <div v-for="(item, index) in slotProps.items" :key="index">
                    <div class="flex items-center gap-2">
                        <div v-html="getIcon(item.component).svg" class="w-5 h-5"></div>
                        <div class="flex flex-col">
                            <span>{{ item.file }}</span>
                            <span class="text-xs text-gray-500">{{ item.location }}</span>
                        </div>
                    </div>
                    <div>
                        <a :href="item.ruleDocLink" target="_blank">{{ item.rule }}</a>
                        <Chip v-for="tag in item.tags" :key="tag" :label="tag" />
                    </div>
                    <div>
                        {{ item.message }}
                    </div>
                    <!-- TODO: severity, type, impacts, quickFixAvailable, effort - use icons -->
                </div>
            </div>
        </template>
    </DataView>
</template>

<script setup>
import { issues } from '~/mock/issues-report';
import { getIcon } from 'material-file-icons';

const processedIssues = computed(() => {
    return issues.map(issue => {
        return {
            component: issue.component,
            file: `${issue.component?.match(/[^:/]*$/)?.[0] ?? ''}:${issue.line}`,
            location: issue.component?.replace(/[^:/]*$/, '').replace(/[:/]$/, '') ?? '',
            fileType:  issue.component?.match(/\.([^.]*)$/)?.[1] ?? '',
            author: issue.author,
            rule: issue.rule,
            ruleDocLink: `https://next.sonarqube.com/sonarqube/coding_rules?open=${encodeURIComponent(issue.rule)}&rule_key=${encodeURIComponent(issue.rule)}`,
            severity: issue.severity,
            type: issue.type,
            impacts: Object.fromEntries(issue.impacts.split(', ').map((impact) => impact.split('='))),
            tags: issue.tags.split(' / '),
            message: issue.message,
            quickFixAvailable: issue.quickFixAvailable,
            effort: issue.effort,
        };
    });
});
</script>