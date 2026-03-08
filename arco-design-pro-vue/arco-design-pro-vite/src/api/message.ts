export interface MessageRecord {
  id: number;
  type: string;
  title: string;
  subTitle: string;
  avatar?: string;
  content: string;
  time: string;
  status: 0 | 1;
  messageType?: number;
}

export type MessageListType = MessageRecord[];

interface MessageListResponse {
  data: MessageListType;
}

export function queryMessageList(): Promise<MessageListResponse> {
  return Promise.resolve({ data: [] });
}

export function setMessageStatus(data: { ids: number[] }): Promise<MessageListResponse> {
  return Promise.resolve({ data: [] });
}
