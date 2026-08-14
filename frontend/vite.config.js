import { defineConfig, loadEnv } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig(({ mode }) => {
  // 1. 현재 모드(development 등)에 맞는 .env 파일의 환경 변수를 로드합니다.
  const env = loadEnv(mode, process.cwd(), '');

  return {
    plugins: [react()],
    server: {
      // 프론트엔드 개발 서버 포트를 5173으로 명시적 고정
      port: 5173, 
      proxy: {
        // 2. '/api'로 시작하는 모든 HTTP 요청을 가로챕니다.
        '/api': {
          // 3. .env.development에 설정된 대상(http://localhost:8080)으로 요청을 포워딩합니다.
          target: env.VITE_API_BASE_URL,
          
          // 대상 서버(Spring Boot)의 호스트 헤더를 target URL로 변경하여 CORS 에러를 회피합니다.
          changeOrigin: true, 
          
          // 로컬 환경은 HTTPS가 아닌 HTTP 통신이므로 SSL 인증서 검증을 비활성화합니다.
          secure: false, 
          
          // 💡 주의: 백엔드 Spring Boot 컨트롤러에 '@RequestMapping("/api")'가 
          // 설정되어 있지 않다면 아래 주석을 해제하여 '/api' 경로를 제거하고 보내야 합니다.
          // rewrite: (path) => path.replace(/^\/api/, ''),
        },
      },
    },
  };
});