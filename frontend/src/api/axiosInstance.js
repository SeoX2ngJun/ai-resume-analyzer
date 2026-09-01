import axios from 'axios';

// const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || '';

// 1. 공통 설정을 가진 Axios 인스턴스 생성
const axiosInstance = axios.create({
  baseURL: '/api',
  timeout: 60000, // AI 분석 대기 시간을 고려하여 60초로 설정
 
});

// 2. 요청 인터셉터: 프론트엔드에서 백엔드로 요청 보내기 직전에 작동함
axiosInstance.interceptors.request.use(
  (config) => {
    // 로컬 스토리지에서 토큰을 꺼내 모든 요청 헤더에 자동 주입
    const token = localStorage.getItem('accessToken');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`; 
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 3. 응답 인터셉터: 백엔드에서 응답이 온 직후, 화면에 전달되기 전에 작동
axiosInstance.interceptors.response.use(
  (response) => {
    // 성공 시 컴포넌트 코드 간결화를 위해 data 객체만 반환
    return response.data;
  },
  (error) => {
    // 백엔드 에러 응답 코드가 넘어온 경우 공통 처리 규칙
    if (error.response) {
      const { status, data } = error.response;
      
      // 명세서에 정의된 커스텀 에러 규격 추출
      const errorCode = data?.errorCode;
      const errorMessage = data?.errorMessage;

      if (errorCode && errorMessage) {
        console.error(`[API Error - ${errorCode}] ${errorMessage}`);
      }

      switch (status) {
        case 400: // 파일 크기 초과, 확장자 오류 등 (컴포넌트로 에러 패스)
          break;
        case 401: // 인증되지 않은 사용자
          console.error('로그인이 만료되었거나 권한이 없습니다. 로그인 페이지로 이동합니다.');
          localStorage.removeItem('accessToken');
          // 필요 시 라우팅 연동 로직 추가
          break;
        case 500: // 백엔드 서버 에러
          console.error('백엔드 시스템 내부에 오류가 발생했습니다. 잠시 후 시도해주세요.');
          break;
        default:
          console.error('통신 중 알 수 없는 에러가 발생했습니다.');
      }
      
      // 커스텀 에러 객체가 존재하면 전달, 없으면 원본 에러 전달
      return Promise.reject(errorCode ? { errorCode, errorMessage } : error);
      
    } else if (error.request) {
      console.error('서버 엔진으로부터 응답을 받을 수 없습니다. 백엔드 구동 상태를 점검하십시오.');
    }
    
    return Promise.reject(error); 
  }
);

export default axiosInstance;