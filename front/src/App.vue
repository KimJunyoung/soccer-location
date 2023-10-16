<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
import HelloWorld from './components/HelloWorld.vue'
</script>

<template>
  <header>
      <nav>
      <div>
        <RouterLink to="/">Home</RouterLink>
        </div>

        <div>
        <RouterLink to="/write">글 작성</RouterLink>
        </div>

      </nav>
  </header>

  <RouterView />
</template>

<style>
</style>