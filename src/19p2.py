import functools


@functools.cache
def num_possibilities(pattern: str) -> int:
    prefixes = [d for d in designs if pattern.startswith(d)]
    cnt = 0
    for p in prefixes:
        cnt += 1 if pattern == p else num_possibilities(pattern.removeprefix(p))
    return cnt


if __name__ == "__main__":
    with open("../inp/19.txt", 'r') as f:
        lines = f.readlines()

    designs = set(lines[0].strip().split(", "))
    patterns = set(l.strip() for l in lines[2:])

    out = 0
    for pat in patterns:
        out += num_possibilities(pat)
    print(f"Final result: {out}")
