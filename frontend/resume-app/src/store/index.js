// 통합 스토어 구성
// 생성한 슬라이스들을 하나의 중앙 스토어에 등록


import { configureStore } from '@reduxjs/toolkit';
import resumeReducer from './slices/resumeSlice';

export const store = configureStore({
  reducer: {
    resume: resumeReducer,
  },
});