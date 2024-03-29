interface User {
  id: number;
  nickname: string;
  email: string;
  image: string;
  followingCount: number;
  followerCount: number;
}

interface SongInfo {
  id: number;
  title: string;
  mr: string;
  image: string;
}

interface ReviewInfo {
  id: number;
  content: string;
  userId: number;
  nickname: string;
  image: string;
}

interface LyricInfo {
  id: number;
  startTime: string;
  endTime: string;
  content: string;
  sequence: number;
  partDto: PartInfo;
}

interface PartInfo {
  id: number;
  image: string;
  name: string;
}

interface Noraebang {
  id: number;
  content: string;
  record: string;
  hit: number;
  likeCount: number;
  songDto: SongInfo;
  userDto: User;
  createdAt: Date;
  likeExsits: boolean;
  noraebangreviewDtos: ReviewInfo[];
  lyricDtos: LyricInfo[];
}

interface Response {
  resultCode: string;
  message: string;
  data: Noraebang;
}

export type { Response, Noraebang };
