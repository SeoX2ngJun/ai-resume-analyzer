// 자소서 송신 함수
// 화면에서 넘겨받은 자소서 파일을 FromData 바이너리 규격으로 포장해 백엔드로 전송import axiosInstance from './axiosInstance';

import axiosInstance from './axiosInstance';

export const uploadResumeAPI = (file) => {
  const formData = new FormData();
  formData.append('file', file); // 백엔드 수신 key값 명칭: 'file'

  return axiosInstance.post('/resumes/upload', formData);
};