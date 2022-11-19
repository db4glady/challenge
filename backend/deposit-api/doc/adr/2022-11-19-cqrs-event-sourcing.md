# Event sourcing

# Status
accepted

# Context
This project is a challenge proposed by Glady during the recruitment process.
As I have to spent some time on this project, I would like to test a library I have never used before.
In addition, I think that CQRS and event sourcing patterns match with the services I have to write.

# Decision
Use event sourcing with axon

# Consequences
* The unit tests will be difficult on some code portion as axon is not designed to be mocked easily.
* Integration tests should be written to test the aggregates properly.