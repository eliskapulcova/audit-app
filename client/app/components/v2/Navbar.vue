<template>
  <nav class="fixed top-0 left-0 right-0 z-50 bg-slate-900/95 backdrop-blur-sm border-b border-slate-800 shadow-lg">
    <div class="max-w-[1920px] mx-auto px-4 sm:px-6 lg:px-8">
      <div class="flex items-center justify-between h-16">
        <div class="flex items-center gap-6">
          <NuxtLink to="/" class="flex items-center gap-3 group">
            <div class="bg-gradient-to-br from-cyan-500 to-blue-500 p-2 rounded-lg group-hover:scale-110 transition-transform">
              <Icon name="lucide:code-2" class="w-6 h-6 text-white" />
            </div>
            <div>
              <div class="text-lg font-bold text-white group-hover:text-cyan-400 transition-colors">Quanti</div>
              <div class="text-xs text-slate-400">Code Quality Hub</div>
            </div>
          </NuxtLink>

          <div class="hidden md:flex items-center gap-4">
            <NuxtLink
              to="/v2"
              :class="[ 'flex items-center gap-2 px-3 py-2 rounded-lg transition-colors', isOverview ? 'bg-cyan-500/20 text-cyan-400' : 'text-slate-400 hover:text-white hover:bg-slate-800' ]"
            >
              <Icon name="lucide:layout-grid" class="w-4 h-4" />
              <span class="text-sm font-medium">All Projects</span>
            </NuxtLink>

            <div v-if="!isOverview" class="flex items-center gap-2 px-3 py-2 bg-slate-800 rounded-lg">
              <Icon name="lucide:folder-open" class="w-4 h-4 text-cyan-400" />
              <span class="text-sm font-medium text-white">Project Details</span>
            </div>
          </div>
        </div>

        <div class="flex items-center gap-4">
          <div class="hidden sm:flex items-center gap-2 text-sm text-slate-400">
            <span>Last scan:</span>
            <span class="text-cyan-400 font-medium">{{ formattedDate }}</span>
          </div>
        </div>
      </div>
    </div>
  </nav>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { useRoute } from 'vue-router';

const props = defineProps<{
  lastScanDate: string;
}>();

const route = useRoute();
const isOverview = computed(() => route.path === '/');

const formattedDate = computed(() => {
  return new Date(props.lastScanDate).toLocaleString('en-US', {
    month: 'short',
    day: 'numeric',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  });
});
</script>
