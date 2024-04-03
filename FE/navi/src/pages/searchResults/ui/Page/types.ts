interface User {
  id: number;
  nickname: string;
  email: string;
  image: string;
  followingCount: number;
  followerCount: number;
  username: string;
  role: string;
}

interface Part {
  id: number;
  image: string;
  name: string;
}

interface SongInfo {
  songPk: number;
  title: string;
  image: string;
  mr: string;
}

interface CoverUser {
  id: number;
  userDto: User;
  partDto: Part;
}

interface CoverItem {
  id: number;
  title: string;
  video?: string;
  thumbnail: string;
  hit?: number;
  likeCount?: number;
  songDto: SongInfo;
  createdAt?: string;
  coverReviewDots?: [];
  coverUserDtos: CoverUser;
  likeExsits?: boolean;
}

interface CoverList {
  data: CoverItem[];
}

interface NoraebangItem {
  id: number;
  content?: string;
  record?: string;
  hit?: number;
  likeCount?: number;
  songDto: SongInfo;
  userDto: User;
  createdAt?: Date;
  roraebnagLikeDtos?: null;
  noraebangReviewDtos?: null;
}

interface NoraebangList {
  data: NoraebangItem[];
}

interface Response {
  resultCode: string;
  message: string;
  data: any;
}

export type { Response, NoraebangList, CoverList, CoverUser, User };
