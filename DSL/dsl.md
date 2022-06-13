
# AMon Domain-Specific Monitoring Adaptation Language

To facilitate the specification of adaptation rules for a runtime monitoring environment, we created a domain-specific language that provides capabilities for defining different types of adaptation rules and specifying assumptions about the monitoring data.

We include two types of rules:

-. Preplanned Rules that are triggered based on different states of the system, or of its devices (e.g., a UAV being in a "takeoff" state versus "flying")
-. Ubiquitous Rules that are triggered by exceptions (e.g., a low battery warning) or other factors.

In addition to the specification of rules, we also support the description of 
- Assumptions about the system and its environment.
- Default Values



## Preplanned Rules

Preplanned adaptations of the monitoring environment may occur as the CPS  changes state, and these adaptations may require changing the monitoring location from the edge device (e.g., onboard the UAV) to a  centralized monitoring component.
Such rules can either apply _globally_, or to _specific devices_ (e.g., a group of UAVs performing a joint mission, or even individual devices identified by their respective id).
The _Context_ element in the _Trigger_ condition indicates the UAV's current state. 
Subsequently, each  _Monitor_ entry denotes a property item (i.e., a message) that is collected and distributed by the monitoring system. 


![image](https://user-images.githubusercontent.com/24531486/173364191-733f8ffb-e904-4812-9ef8-4f8a11567f8e.png)



Monitor specifications define the _Scope_ i.e., whether the data is processed locally on the device _scope local_, or sent to a central server (_scope central_) for processing, constraint checking, or further analysis.
The _Period_ further specifies how often data is sent from, for example, the edge device to the central server.


## Ubiquitous Rules

In addition to the preplanned course of events, modeled in a state transition diagram, deviations from the intended behavior may occur. In such cases, the monitoring infrastructure must adapt the monitors to provide sufficient information according to the current state of the system.
For example, a sudden drop in a UAV's battery voltage level might require a corresponding increase in monitoring periods whilst remedial actions are taken to preserve energy and ensure the UAV's safe return or landing.

![image](https://user-images.githubusercontent.com/24531486/173364278-535dcc2c-35fc-43f8-894c-a72eba576a17.png)

![image](https://user-images.githubusercontent.com/24531486/173364379-012afc36-6e4b-4df1-b5a3-3a820233cd7f.png)

_Ubiquitous_ rules can be triggered at any time, regardless of the state of the system, and will supersede the original adaptation rules for a certain state. 
For example, given a sudden drop in battery voltage, the monitoring period might be decreased for less essential properties in order to preserve battery, but increased for essential battery data. When the ``battery_state_warning`` event is triggered, the corresponding ubiquitous rule supersedes the monitoring behavior of the UAV's current context.
If at some point the ubiquitous state no longer applies (e.g., because the battery warning was a temporary glitch), the UAV resumes its normal mode of operation.

In these contexts, an _EXIT_ event for a ubiquitous state indicates that the ubiquitous rule should no longer apply, and the monitoring infrastructure resumes its "normal" monitoring duty by selecting and applying preplanned rules.

![image](https://user-images.githubusercontent.com/24531486/173364458-a34c5cc8-3c61-4d3c-aad0-ac93b82f4d19.png)



## Default Values


To make rules more precise and avoid the need for specifying long lists of properties in each rule, we support _Default_ value definitions.


Default values apply globally across all rules for specific properties and can assume one of two values. If a value is _off_, then unless a rule explicitly specifies a monitor for that event, the monitoring is automatically turned off. If a default value is _keep_, then the monitor for this event remains unchanged and retains the period and scope from the previously executed rule. Introducing the default value concept allows to greatly reduce the number of monitors that need to be specified as part of each rule.



![image](https://user-images.githubusercontent.com/24531486/173364525-2e713038-12d3-49a1-8aa2-ee9617648f91.png)



## Assumptions


_Assumptions_ are used to specify properties, such as the assumed monitoring period, that should not be violated by the monitoring rules.
Assumptions can be used to specify limitations on the monitoring rules, with regards to the minimum period and scope.
The minimum period in the assumption specifies how often the monitored system guarantees updated values.
If an assumption specifies a minimum period of $x$ for a property and a monitoring rule requires the property to be monitored more often than that (with a period of y < x), the assumption is violated.

We validate assumptions through a combination of static rules validation and additional runtime checks.
Assumptions can also be specified on constraints to determine which properties are required and whether the constraint is evaluated locally or centrally. In this context, constraints are checked on the specified rules (static validation) and on the runtime data to check that the system complies to its expected behavior and that stated assumptions hold.




![image](https://user-images.githubusercontent.com/24531486/173364593-be9ed830-9398-4a40-ba70-3d781ac7620f.png)





### Grammar

[XText DSL File](Dsl.xtext)


```Model: 	namespace=Namespace contracts+=Assumption* defaults+=DefaultValues* prules+=MonitoringRule*  grules+=GlobalRule* constraints+=ConstraintRule*;```

``Namespace:	'namespace' namespace=STRING ';';``


``Assumption: 	'Assumption' name=STRING frequency=MinFrequency  ';';``

``DefaultValues:	'Default' name=STRING value=State ';';``

`` State: 	state=(OFF|KEEP);``



``MinFrequency:	'MIN_PERIOD:' frequency=DOUBLE 'seconds';``

``ConstraintRule:	'Constraint' name=STRING scope=Scope  'requiresmonitor' monitors+=STRING* ';';``

``Applicability:	'applies' trigger=(ApplicationID | ApplicationGlobal | ApplicationGroup);``

``ApplicationID:	'for' 'device' '[' event=STRING ']';``

``ApplicationGroup:	'for' 'group' '(' group=STRING ')';``

``ApplicationGlobal:	sc='forall';``

``Scope:	'scope' scope=('local' | 'central');``

``MonitoringRule:	'Rule' 'PREPLANNED' name=STRING applicability=Applicability  trigger=Trigger monitors+=Monitor* ';';``
	
``GlobalRule:	'Rule' 'UBIQUITOUS' name=STRING applicability=Applicability ('salience' salience=INT)?  trigger=Trigger monitors+=Monitor*  ';';``
	

``Trigger:	'Trigger' trigger=(Event | Context | Data);``

``Context:	'Context' '[' context=STRING ']' status?=Status?;``

``Status:	'ENTRY' | 'EXIT';``

``Event:	'Event' '[' event=STRING ']' ('and' 'Event' '[' ontop+=STRING ']')* ;``

``Data:	'Data' '[' value=STRING 'when' condition=ValueCondition ']';``


``Monitor:	'Monitor' name=STRING ':' changes+=ChangeType*;``

``ChangeType:	ChangeFrequency | ChangeState | ChangeScope | ChangeReportingFrequency;``

``ChangeScope:	'CHANGE_SCOPE:' scope=Scope;``

``ChangeState:	'CHANGE_STATE:' scope=(ON | OFF);``

``ChangeFrequency:	'CHANGE_PERIOD:' 'every' frequency=DOUBLE 'seconds';``

``ChangeReportingFrequency:	'CHANGE_REPORTING_PERIOD:' 'every' frequency=DOUBLE 'seconds';``
