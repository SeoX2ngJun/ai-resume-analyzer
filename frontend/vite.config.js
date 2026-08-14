import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173, // Frontend 로컬 개발 포트 고정
    proxy: {
      // 브라우저에서 /api로 시작하는 요청 가져오기
      '/api': {
        target: 'http://localhost::8080', // 백엔드 주소
        changeOrigin: true,
        // 백엔드 엔드포인트 규격에 맞게 /api 접두사를 제거하여 전달
        rewrite: (path) => path.replace(/^\/api/,''),
      },
    },
  },
})
