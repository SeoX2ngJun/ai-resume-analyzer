import axiosInstance from './axiosinstance';

/**
 * 1. 자기소개서 파일 업로드 및 AI 분석 요청
 * @param {File} fileObject - 사용자가 선택한 PDF/Word 파일
 * @returns {Promise<Object>} 분석 완료된 문서 정보 (documentId 등)
 */

export const uploadResumeApi = async (fileObject) => {
  const formData = new FormData();
  formData.append('file', fileObject);

  // axiosInstance 인터셉터가 response.data를 반환하므로 데이터 추출 과정 생략
  //return await axiosInstance.post('/api/v1/documents/upload', formData, {
  //  headers: {
  //   'Content-Type': 'multipart/form-data',
  //  },
  //})
  return await axiosInstance.post('/api/v1/documents/upload', formData);
};

/**
 * 2. AI 분석 결과 리포트 상세 조회
 * @param {number|string} documentId - 조회할 문서의 고유 ID
 * @returns {Promise<Object>} AI 리포트 상세 데이터
 */
export const getDocumentReportApi = async (documentId) => {
  return await axiosInstance.get(`/api/v1/documents/${documentId}/report`);
};

/**
 * 3. 메인 화면용 과거 자소서 분석 내역 목록 조회
 * @returns {Promise<Array>} 과거 분석 내역 리스트
 */
export const getDocumentListApi = async () => {
  return await axiosInstance.get('/api/v1/documents');
};