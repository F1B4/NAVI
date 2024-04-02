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
  id: number;
  title: string;
  mr: string;
  image: string;
}

interface CoverUserDto {
  id: number;
  userDto: User;
  partDto: Part;
}

interface CoverDto {
  id: number;
  title: string;
  video: string;
  thumbnail: string;
  hit: number;
  likeCount: number;
  songDto: SongInfo;
  createdAt: Date;
  coverReviewDtos: null;
  coverUserDtos: CoverUserDto[];
  likeExists: null;
}

interface NoraebangDto {
  id: number;
  content: null;
  record: null;
  hit: null;
  likeCount: null;
  songDto: SongInfo;
  userDto: User;
  CreatedAt: Date;
  noraebangLikeDtos: null;
  noraebangReviewDtos: null;
}

interface CoverLikeDto {
  id: number;
  coverDto: CoverDto;
}

interface NoraebangLikeDto {
  id: number;
  noraebangDto: NoraebangDto;
}

interface Profile {
  nickname: string;
  image: string;
  followingCount: number;
  followerCount: number;
  isFollow: boolean;
  coverDtos: CoverDto[];
  noraebangDtos: NoraebangDto[];
  coverLikeDtos: CoverLikeDto[];
  noraebangLikeDtos: NoraebangLikeDto[];
}

interface Response {
  resultCode: string;
  message: string;
  data: Profile;
}

export type {
  Response,
  Profile,
  CoverDto,
  CoverUserDto,
  NoraebangDto,
  CoverLikeDto,
  NoraebangLikeDto,
};
