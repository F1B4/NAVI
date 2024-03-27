interface User {
  id: number;
  nickname: string;
  email: string;
  image: string;
  followingCount: number;
  followerCount: number;
}

interface SongInfo {
  songPk: number;
  title: string;
  image: string;
  users: string;
}

interface CoverUser {
  id: number;
  userDto: User;
  partDto: {
    id: number;
    image: string;
    name: string;
  };
}

interface CoverItem {
  id: number;
  title: string;
  thumbnail: string;
  hit: number;
  likeCount: number;
  songDto: SongInfo;
  createdAt: string;
  coverReviewDots?: [];
  coverUserDtos: CoverUser[];
  likeExsits: boolean;
}

interface CoverList {
  data: CoverItem[];
}

interface Response {
  resultCode: string;
  message: string;
  data: CoverList;
}

export type { Response, CoverList };
