import axiosInstance from './axiosInstance';

/**
 * 자소서 파일 업로드 및 AI 분석 요청
 * @param {File} fileObject - 사용자가 선택한 PDF/Word 파일
 * @returns {Promise<Object>} 백엔드로부터 반환된 AI 분석 리포트 JSON 데이터
 */
export const uploadResumeApi = async (fileObject) => {
  const formData = new FormData();
  // 백엔드 Spring Boot 컨트롤러의 @RequestPart("file") 또는 @RequestParam("file") 수신 key와 일치
  formData.append('file', fileObject);

  const response = await axiosInstance.post('/api/resumes/upload', formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  });

  return response.data;
};