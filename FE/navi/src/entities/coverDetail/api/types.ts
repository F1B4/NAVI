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
  userDto: User;
}

interface CoverInfo {
  id: number;
  userDto: User;
  partDto: PartInfo;
}

interface PartInfo {
  id: number;
  image: string;
  name: string;
}

interface Cover {
  id: number;
  title: string;
  video: string;
  hit: number;
  likeCount: number;
  songDto: SongInfo;
  createdAt: Date;
  coverReviewDtos: ReviewInfo[];
  coverUserDtos: CoverInfo[];
  likeExsits: boolean;
}

interface Response {
  resultCode: string;
  message: string;
  data: Cover;
}

export type { Response, Cover, ReviewInfo, User, CoverInfo };
