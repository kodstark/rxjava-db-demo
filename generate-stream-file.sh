#!/bin/bash

# Output file name
output_file="stream2.txt"

# Number of lines to generate
num_lines=100

# Create or overwrite the output file
> "$output_file"

# Generate 100 lines
for i in $(seq 1 $num_lines); do
  # Generate random text (adjust the length of random text if needed)
  random_text=$(head /dev/urandom | tr -dc 'a-zA-Z0-9 ' | head -c 50)

  # Write line number and random text to the file
  echo "$i: $random_text" >> "$output_file"
done

echo "File '$output_file' with $num_lines lines created."
