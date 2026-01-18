import { QueryClient } from '@tanstack/react-query';

export const queryClient = new QueryClient({
    defaultOptions: {
        queries: {
            staleTime: 30_000, // 30 seconds
            refetchInterval: false, // disable automatic refetching
            retry: 3,
        },
    },
});
