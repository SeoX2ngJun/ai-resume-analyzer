import React, { useState, useRef } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { uploadResume, resetResumeState } from '../store/slices/resumeSlice';

const MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB 제한
const ALLOWED_EXTENSIONS = ['pdf', 'doc', 'docx'];

const ResumeUpload = () => {
  const dispatch = useDispatch();
  const { status, reportData, error } = useSelector((state) => state.resume);

  const [selectedFile, setSelectedFile] = useState(null);
  const [isDragOver, setIsDragOver] = useState(false);
  const [validationError, setValidationError] = useState('');
  const fileInputRef = useRef(null);

  // 파일 유효성 검증
  const validateFile = (file) => {
    if (!file) return false;

    const fileExtension = file.name.split('.').pop().toLowerCase();
    if (!ALLOWED_EXTENSIONS.includes(fileExtension)) {
      setValidationError('PDF 또는 Word(.doc, .docx) 파일만 업로드 가능합니다.');
      return false;
    }

    if (file.size > MAX_FILE_SIZE) {
      setValidationError('파일 크기는 최대 10MB를 초과할 수 없습니다.');
      return false;
    }

    setValidationError('');
    return true;
  };

  const handleFileChange = (e) => {
    const file = e.target.files[0];
    if (validateFile(file)) {
      setSelectedFile(file);
    } else {
      setSelectedFile(null);
    }
  };

  const handleDrop = (e) => {
    e.preventDefault();
    setIsDragOver(false);
    const file = e.dataTransfer.files[0];
    if (validateFile(file)) {
      setSelectedFile(file);
    } else {
      setSelectedFile(null);
    }
  };

  const handleDragOver = (e) => {
    e.preventDefault();
    setIsDragOver(true);
  };

  const handleDragLeave = () => {
    setIsDragOver(false);
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!selectedFile) {
      setValidationError('업로드할 파일을 선택해주세요.');
      return;
    }
    dispatch(uploadResume(selectedFile));
  };

  const handleReset = () => {
    setSelectedFile(null);
    setValidationError('');
    dispatch(resetResumeState());
    if (fileInputRef.current) {
      fileInputRef.current.value = '';
    }
  };

  return (
    <div style={{ maxWidth: '600px', margin: '40px auto', padding: '20px', border: '1px solid #e0e0e0', borderRadius: '8px' }}>
      <h2>AI 자기소개서 분석 요청</h2>

      <form onSubmit={handleSubmit}>
        <div
          onDrop={handleDrop}
          onDragOver={handleDragOver}
          onDragLeave={handleDragLeave}
          onClick={() => fileInputRef.current.click()}
          style={{
            border: isDragOver ? '2px dashed #007bff' : '2px dashed #ccc',
            borderRadius: '6px',
            padding: '40px',
            textAlign: 'center',
            backgroundColor: isDragOver ? '#f0f7ff' : '#fafafa',
            cursor: 'pointer',
            marginBottom: '15px',
          }}
        >
          <input
            type="file"
            ref={fileInputRef}
            onChange={handleFileChange}
            accept=".pdf,.doc,.docx"
            style={{ display: 'none' }}
          />
          {selectedFile ? (
            <p style={{ fontWeight: 'bold', color: '#007bff' }}>선택된 파일: {selectedFile.name}</p>
          ) : (
            <p>PDF 또는 Word 파일을 이곳에 드래그하거나 클릭하여 선택하세요. (최대 10MB)</p>
          )}
        </div>

        {validationError && <p style={{ color: 'red', fontSize: '14px' }}>{validationError}</p>}
        {error && <p style={{ color: 'red', fontSize: '14px' }}>서버 에러: {error}</p>}

        <button
          type="submit"
          disabled={status === 'loading' || !selectedFile}
          style={{
            width: '100%',
            padding: '12px',
            backgroundColor: status === 'loading' ? '#cccccc' : '#007bff',
            color: '#fff',
            border: 'none',
            borderRadius: '4px',
            fontSize: '16px',
            cursor: status === 'loading' ? 'not-allowed' : 'pointer',
          }}
        >
          {status === 'loading' ? 'AI 리포트 분석 중...' : '자소서 분석 시작'}
        </button>
      </form>

      {status === 'succeeded' && reportData && (
        <div style={{ marginTop: '30px', padding: '15px', backgroundColor: '#e9f7ef', borderRadius: '4px' }}>
          <h3>AI 분석 리포트 결과</h3>
          <pre style={{ whiteSpace: 'pre-wrap', wordBreak: 'break-all' }}>
            {JSON.stringify(reportData, null, 2)}
          </pre>
          <button onClick={handleReset} style={{ marginTop: '10px', padding: '8px 16px' }}>
            다른 자소서 분석하기
          </button>
        </div>
      )}
    </div>
  );
};

export default ResumeUpload;