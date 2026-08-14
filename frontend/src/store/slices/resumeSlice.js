import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { uploadResumeApi } from '../../api/resumeapi';

// 비동기 자소서 업로드 Thunk 액션
export const uploadResume = createAsyncThunk(
  'resume/uploadResume',
  async (fileObject, { rejectWithValue }) => {
    try {
      // 응답 인터셉터가 response.data를 반환하므로 바로 할당
      const data = await uploadResumeApi(fileObject);
      return data;
    } catch (error) {
      // Axios 공통 인터셉터에서 넘겨준 커스텀 에러 메시지 추출
      return rejectWithValue(
        error.errorMessage || '자소서 업로드 및 분석에 실패했습니다.'
      );
    }
  }
);

const initialState = {
  status: 'idle', // 'idle' | 'loading' | 'succeeded' | 'failed'
  reportData: null,
  error: null,
};

const resumeSlice = createSlice({
  name: 'resume',
  initialState,
  reducers: {
    resetResumeState: (state) => {
      state.status = 'idle';
      state.reportData = null;
      state.error = null;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(uploadResume.pending, (state) => {
        state.status = 'loading';
        state.error = null;
      })
      .addCase(uploadResume.fulfilled, (state, action) => {
        state.status = 'succeeded';
        state.reportData = action.payload; // 백엔드가 준 JSON 결과물 저장
      })
      .addCase(uploadResume.rejected, (state, action) => {
        state.status = 'failed';
        state.error = action.payload;
      });
  },
});

export const { resetResumeState } = resumeSlice.actions;
export default resumeSlice.reducer;