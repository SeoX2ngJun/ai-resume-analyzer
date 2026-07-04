import axios from 'axios';

// .env.development에 작성한 'api' 주소를 엔진에서 로드
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'api';

// 1. 공통 설정을 가진 Axios 인스턴스 생성
const axiosInstance = axios.create({
    baseURL: API_BASE_URL,
    timeout: 10000, // 서버 응답시간 제한(10초)
    header: {
        'Content-Type': 'application/json',
    } ,
});

//2. 요청 인터셉터: 프론트엔드에서 백엔드로 요청 보내기 직전에 작동함
axiosInstance.interceptors.request.use(
    (config) => {
    // 로그인 구현 시 웹 브라우저에 저장된 토근을 자동으로 꺼내
    // 모든 백엔드 요청 헤더에 자동으로 주입해주는 뼈대
    const token = localStorage.getItem('accessToken');
    if(token){
        config.headers.Authorization = 'Bearer ${token}';
    }
    return config;
},
(error) => {
    return Promise.reject(error);
}
);

// 3. 응답 인터셉터: 백엔드에서 응답이 온 직후, 화면에 전달되기 전에 작동
axiosInstance.interceptors.response.use(
    (response) => response,
    (error) => {
        // 백엔드 에러 응답 코드가 넘어온 경우 (400,401,500) 공통 처리 규칙
        if(error.response){
            const{status} = error.response;

            switch(status){
                case 401: // 인증되지 않은 사용자
                    console.error('로그인이 만료되었거나 권한이 없습니다. 로그인 페이지로 이동합니다.');
                    localStorage.removeItem('accessToken');
                    //필요 시 window.location.href = '/login'; 구조로 라우팅 연동 예정임
                    break;
                case 500: // 백엔드 서부 에러 발생 시
                    console.error('백엔드 시스템 내부에 오류가 발생했습니다. 잠시 후 시도해주세요.');
                    break;
                default:
                    console.error('통신 중 알 수 없는 에러가 발생했습니다.');
            }
        }else if(error.request){
            //백엔드 서버가 다운되거나 네트워크가 단절된 경우
            console.error('서버 엔진으로부터 응답을 받을 수 없습니다. 백엔드 구동 상태를 점검하십시오.');
        }
        return Promise.reject
    }
);

export default axiosInstance;