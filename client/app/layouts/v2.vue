<template>
    <div class="min-h-screen bg-slate-950">
        <Navbar :last-scan-date="lastScanDate" />
        <main class="max-w-[1920px] mx-auto px-4 sm:px-6 lg:px-8 pt-24 pb-12">
            <slot />
        </main>
    </div>
</template>

<script setup lang="ts">
import Navbar from '~/components/v2/Navbar.vue';
import { lastScanDate } from '~/mock/v2/mock-data'
</script>
