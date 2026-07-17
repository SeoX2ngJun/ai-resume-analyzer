// 업로드 UI 컴포넌트 작성
// 사용자가 시각적으로 드래그 앤 드롭을 할 수 있는 영역과 Redux 상태 연동을 완비한 실무 표준 컴포넌트 코드

import React, { useState, useRef } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { uploadAndAnalyzeResume } from '../store/slices/resumeSlice';

const ResumeUpload = () => {
  const [selectedFile, setSelectedFile] = useState(null);
  const [isDragActive, setIsDragActive] = useState(false);
  const fileInputRef = useRef(null);
  
  const dispatch = useDispatch();
  // Redux Store로부터 업로드 및 분석 상태 구독
  const { loading, error, report } = useSelector((state) => state.resume);

  // 파일 유효성 검증 함수 (10MB 제한, PDF/Word 확장자 제한)
  const validateAndSetFile = (file) => {
    if (!file) return;

    const allowedExtensions = /(\.pdf|\.docx|\.doc)$/i;
    if (!allowedExtensions.exec(file.name)) {
      alert('PDF 또는 Word(.doc, .docx) 파일만 업로드할 수 있습니다.');
      return;
    }

    const maxFileSize = 10 * 1024 * 1024; // 10MB
    if (file.size > maxFileSize) {
      alert('파일 용량은 최대 10MB를 초과할 수 없습니다.');
      return;
    }

    setSelectedFile(file);
  };

  // 1. 파일 직접 선택 핸들러
  const handleFileChange = (e) => {
    const file = e.target.files[0];
    validateAndSetFile(file);
  };

  // 2. 드래그 앤 드롭 핸들러들
  const handleDrag = (e) => {
    e.preventDefault();
    e.stopPropagation();
    if (e.type === 'dragenter' || e.type === 'dragover') {
      setIsDragActive(true);
    } else if (e.type === 'dragleave') {
      setIsDragActive(false);
    }
  };

  const handleDrop = (e) => {
    e.preventDefault();
    e.stopPropagation();
    setIsDragActive(false);

    if (e.dataTransfer.files && e.dataTransfer.files[0]) {
      validateAndSetFile(e.dataTransfer.files[0]);
    }
  };

  // 파일 입력 트리거
  const onButtonClick = () => {
    fileInputRef.current.click();
  };

  // 3. 백엔드 송신 트리거
  const handleSubmit = (e) => {
    e.preventDefault();
    if (!selectedFile) {
      alert('분석할 자기소개서 파일을 먼저 업로드해 주세요.');
      return;
    }

    // Redux Thunk Action Dispatch 실행
    dispatch(uploadAndAnalyzeResume(selectedFile));
  };

  return (
    <div className="upload-box" style={{ maxWidth: '600px', margin: '40px auto', padding: '20px', fontFamily: 'sans-serif' }}>
      <h2 style={{ textAlign: 'center', marginBottom: '20px' }}>AI 자기소개서 분석기</h2>
      
      <form onSubmit={handleSubmit}>
        <div 
          className={`drag-drop-zone ${isDragActive ? 'active' : ''}`}
          onDragEnter={handleDrag}
          onDragOver={handleDrag}
          onDragLeave={handleDrag}
          onDrop={handleDrop}
          style={{
            border: `2px dashed ${isDragActive ? '#007bff' : '#ccc'}`,
            borderRadius: '8px',
            padding: '40px 20px',
            textAlign: 'center',
            backgroundColor: isDragActive ? '#f0f8ff' : '#fafafa',
            cursor: 'pointer',
            transition: 'all 0.2s ease'
          }}
          onClick={onButtonClick}
        >
          {/* 실제 파일 input은 숨겨두고 div 클릭이나 버튼 클릭으로 연동 */}
          <input 
            ref={fileInputRef}
            type="file" 
            accept=".pdf,.doc,.docx" 
            onChange={handleFileChange} 
            disabled={loading}
            style={{ display: 'none' }}
          />
          
          <p style={{ margin: '0 0 10px 0', fontSize: '16px', color: '#555' }}>
            {selectedFile 
              ? `선택된 파일: ${selectedFile.name}` 
              : '자기소개서(PDF, DOCX) 파일을 이곳에 드래그하거나 클릭하여 선택하세요.'
            }
          </p>
          <button 
            type="button" 
            disabled={loading}
            style={{ padding: '8px 16px', border: '1px solid #999', borderRadius: '4px', background: '#fff', cursor: 'pointer' }}
          >
            파일 찾기
          </button>
        </div>

        <button 
          type="submit" 
          disabled={loading || !selectedFile}
          style={{
            width: '100%',
            marginTop: '20px',
            padding: '12px',
            backgroundColor: '#007bff',
            color: '#fff',
            border: 'none',
            borderRadius: '4px',
            fontSize: '16px',
            fontWeight: 'bold',
            cursor: loading || !selectedFile ? 'not-allowed' : 'pointer',
            opacity: loading || !selectedFile ? 0.6 : 1
          }}
        >
          {loading ? 'OpenAI 분석 엔진 가동 중...' : '자소서 분석 시작하기'}
        </button>
      </form>

      {/* 에러 피드백 */}
      {error && (
        <div style={{ marginTop: '20px', padding: '12px', backgroundColor: '#f8d7da', color: '#721c24', borderRadius: '4px' }}>
          <strong>통신/분석 실패:</strong> {error}
        </div>
      )}

      {/* 로딩 표시 */}
      {loading && (
        <div style={{ marginTop: '20px', textAlign: 'center', color: '#666' }}>
          <p>서버에서 파일을 분석하고 리포트를 생성하는 중입니다.</p>
          <p style={{ fontSize: '12px', color: '#999' }}>대기 시간: 약 10초 ~ 30초 소요</p>
        </div>
      )}

      {/* 분석 결과 데이터 출력 */}
      {report && (
        <div style={{ marginTop: '30px', padding: '20px', backgroundColor: '#e2f0d9', border: '1px solid #a8d08d', borderRadius: '4px' }}>
          <h3 style={{ marginTop: 0, color: '#385723' }}>AI 분석 결과 리포트</h3>
          <pre style={{ whiteSpace: 'pre-wrap', fontSize: '14px', background: '#fff', padding: '15px', borderRadius: '4px', border: '1px solid #d9d9d9' }}>
            {JSON.stringify(report, null, 2)}
          </pre>
        </div>
      )}
    </div>
  );
};

export default ResumeUpload;