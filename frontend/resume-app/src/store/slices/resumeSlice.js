import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { uploadResumeApi } from '../../api/resumeApi';

// 비동기 자소서 업로드 Thunk 액션
export const uploadResume = createAsyncThunk(
  'resume/uploadResume',
  async (fileObject, { rejectWithValue }) => {
    try {
      const data = await uploadResumeApi(fileObject);
      return data;
    } catch (error) {
      // Axios 인터셉터에서 정제된 에러 메시지 반환
      return rejectWithValue(
        error.response?.data?.message || '자소서 업로드 및 분석에 실패했습니다.'
      );
    }
  }
);

const initialState = {
  file: null,
  status: 'idle', // 'idle' | 'loading' | 'succeeded' | 'failed'
  reportData: null,
  error: null,
};

const resumeSlice = createSlice({
  name: 'resume',
  initialState,
  reducers: {
    resetResumeState: (state) => {
      state.file = null;
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
        state.reportData = action.payload;
      })
      .addCase(uploadResume.rejected, (state, action) => {
        state.status = 'failed';
        state.error = action.payload;
      });
  },
});

export const { resetResumeState } = resumeSlice.actions;
export default resumeSlice.reducer;