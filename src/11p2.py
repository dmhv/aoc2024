import time
from functools import cache

nums = [1117, 0, 8, 21078, 2389032, 142881, 93, 385]


@cache
def count_stones(stone: int, num_steps: int) -> int:
    if num_steps == 0: return 1
    if stone == 0: return count_stones(1, num_steps - 1)
    if len(str(stone)) % 2 == 0:
        left = int(str(stone)[0:len(str(stone)) // 2])
        right = int(str(stone)[len(str(stone)) // 2:])
        return count_stones(left, num_steps - 1) + count_stones(right, num_steps - 1)
    return count_stones(stone * 2024, num_steps - 1)


if __name__ == "__main__":
    res = 0
    for n in nums:
        tic = time.time()
        res += count_stones(n, 75)
        print(f"[{n}] -> {res}, done in {time.time() - tic:.5f} seconds")

    print(f"P2: {res}")
